from fastapi.middleware.cors import CORSMiddleware


import torch
import numpy as np
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import pickle
import torch.nn as nn

# 3. FastAPI 인스턴스 생성
app = FastAPI(title="Korean Real Estate Price Prediction API")
origins = [
    # "http://localhost:5173",  # Vue 개발 서버 주소
    "*"   # 둘 다 추가하면 좋음
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,          # 위 origins 리스트 사용
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 1. 저장된 객체 불러오기
MODEL_PATH = "model.pth"
SCALER_PATH = "scaler.pkl"
REGION_DONG_TO_IDX_PATH = "region_dong_to_idx.pkl"
REGION_DONG_NAMES_PATH = "region_dong_names.pkl"
DF_GROUPED_PATH = "df_grouped.pkl"
MEAN_DONG_GROUPED_PATH="mean_price_per_sqm.pkl"

class MultiRegionDongGRUD(nn.Module):
    def __init__(self, input_dim, hidden_dim, num_region_dong, emb_dim=8):
        super().__init__()
        self.region_dong_emb = nn.Embedding(num_region_dong, emb_dim)
        self.gru = nn.GRU(input_dim + emb_dim, hidden_dim, batch_first=True)
        # 저장 당시 구조에 맞게 Sequential로 래핑
        self.fc = nn.Sequential(nn.Linear(hidden_dim, 1))
    def forward(self, x, region_dong_idx):
        region_dong_emb = self.region_dong_emb(region_dong_idx).unsqueeze(1)
        region_dong_emb = region_dong_emb.expand(-1, x.size(1), -1)
        x_cat = torch.cat([x, region_dong_emb], dim=2)
        out, _ = self.gru(x_cat)
        out = self.fc(out[:, -1, :])
        return out

# 2. 모델 및 부가 객체 로드
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

with open(SCALER_PATH, "rb") as f:
    scaler = joblib.load(f)
with open(REGION_DONG_TO_IDX_PATH, "rb") as f:
    region_dong_to_idx = pickle.load(f)
with open(REGION_DONG_NAMES_PATH, "rb") as f:
    region_dong_names = pickle.load(f)
with open(DF_GROUPED_PATH, "rb") as f:
    df_grouped = pickle.load(f)
with open(MEAN_DONG_GROUPED_PATH, "rb") as f:
    dong_grouped = pickle.load(f)

input_dim = 3
emb_dim = 8
hidden_dim = 32
num_region_dong = len(region_dong_names)

model = MultiRegionDongGRUD(input_dim, hidden_dim, num_region_dong, emb_dim)
model.load_state_dict(torch.load(MODEL_PATH, map_location=device))
model = model.to(device)
model.eval()

# 2-1. 제곱미터당 실거래가 컬럼 추가 (이미 있으면 무시)
if '제곱미터당_실거래가' not in df_grouped.columns:
    if '거래금액_filled' in df_grouped.columns:
        df_grouped['제곱미터당_실거래가'] = df_grouped['거래금액_filled'] / df_grouped['전용면적']
    elif '제곱미터당_실거래가_filled' in df_grouped.columns:
        df_grouped['제곱미터당_실거래가'] = df_grouped['제곱미터당_실거래가_filled']
    else:
        raise ValueError("df_grouped에 '거래금액_filled'나 '제곱미터당_실거래가_filled' 컬럼이 없습니다.")

if '연도' not in df_grouped.columns:
    if '거래일' in df_grouped.columns:
        df_grouped['연도'] = pd.to_datetime(df_grouped['거래일']).dt.year
    else:
        raise ValueError("df_grouped에 '거래일' 컬럼이 없습니다.")



# 4. 요청/응답 모델 정의
class PredictRequest(BaseModel):
    region_dong_name: str
    target_year: int

class PredictResponse(BaseModel):
    region_dong_name: str
    target_year: int
    predicted_prices: list
    dates: list
    mean_price: float

class ActualPricePerSqmRequest(BaseModel):
    region_dong_name: str

class ActualPricePerSqmResponse(BaseModel):
    years: list
    mean_price_per_sqm: list

class PredictBarRequest(BaseModel):
    region_dong_name: str
    target_year: int

class PredictBarResponse(BaseModel):
    years: list
    avgPrices: list
    isPredicted: list
    predictedPrice: float
    accuracy: float

@app.post("/predict_bar", response_model=PredictBarResponse)
def predict_bar(request: PredictBarRequest):
    try:
        region_dong_name = request.region_dong_name
        target_year = request.target_year

        # 1. 지역코드, 법정동 분리
        try:
            code, dong = region_dong_name.split('_')
        except:
            raise HTTPException(status_code=400, detail="region_dong_name 형식 오류")

        # 2. mean_price_per_sqm.pkl에서 해당 지역동 연도별 평균 실거래가 추출
        code = float(code)  # 지역코드를 float으로 변환 (11320.0)
        dong = dong.strip()
        target_year = float(target_year)


        group = dong_grouped[
            (dong_grouped['지역코드'] == code) &
            (dong_grouped['법정동'].str.strip() == dong)
        ]
        print(group[['연도', '제곱미터당_실거래가_평균']])
        print(group['연도'].unique())


        if group.empty:
            print(f"[DEBUG] code={code}, dong={dong}")
            print("[DEBUG] dong_grouped 지역코드 unique:", dong_grouped['지역코드'].unique()[:10])
            print("[DEBUG] dong_grouped 법정동 unique:", dong_grouped['법정동'].unique()[:10])
            raise HTTPException(status_code=404, detail="해당 지역동의 데이터가 없습니다.")

        years = group['연도'].tolist()
        avgPrices = group['제곱미터당_실거래가_평균'].tolist()
        isPredicted = [False] * len(years)

        # 2. target_year 예측치 추가
        pred_dates, pred_prices = predict_year_price(
            model=model,
            region_dong_name=region_dong_name,
            target_year=int(target_year),
            df_grouped=df_grouped,
            scaler=scaler,
            region_dong_to_idx=region_dong_to_idx,
            seq_len=30,
            device=device
        )
        mean_pred_price = abs(float(np.mean(pred_prices))) if pred_prices else 0.0

        # 3. 그래프 데이터에 예측 연도 추가
        years.append(target_year)
        avgPrices.append(mean_pred_price)
        isPredicted.append(True)

        years = [int(y) for y in years]
        min_year = int(min(years))
        max_year = int(max(years))
        all_years = [float(y) for y in range(min_year, max_year + 1)]
        year_to_price = dict(zip(years, avgPrices))

        avgPrices_filled = []
        isPredicted_filled = []

        last_value = None
        for y in all_years:
            if y in years:
                value = year_to_price[y]
                is_pred = isPredicted[years.index(y)]
                last_value = value
            else:
                value = last_value  # 이전 값으로 채움
                is_pred = False     # 예측 아님
            avgPrices_filled.append(value)
            isPredicted_filled.append(is_pred)

        # 4. 예측 정확도 등은 임의값 또는 실제 모델에서 계산
        accuracy = 98.97  # 임시값, 실제로는 모델 평가값으로 대체 가능

        return PredictBarResponse(
            years=all_years,
            avgPrices=avgPrices_filled,
            isPredicted=isPredicted_filled,
            predictedPrice=mean_pred_price,
            accuracy=accuracy
        )
    except Exception as e:
        print("[ERROR]", e)
        raise HTTPException(status_code=400, detail=str(e))

# 5. 예측 함수 (제곱미터당 실거래가 반환)
def predict_year_price(model, region_dong_name, target_year, df_grouped, scaler, region_dong_to_idx, seq_len=30, device='cpu'):
    dong_code, dong_name = region_dong_name.split('_')
    dong_code = str(int(float(dong_code)))  # "31110.0" -> 31110 -> "31110"
    print(dong_code)
    region_dong_name = f"{dong_code}_{dong_name}"

    if region_dong_name not in region_dong_to_idx:
        raise ValueError("존재하지 않는 지역동명입니다.")
    region_dong_idx = region_dong_to_idx[region_dong_name]
    group = df_grouped[df_grouped['지역동'] == region_dong_name].copy()
    group = group.set_index('거래일').asfreq('D')
    group['dt'] = group.index.to_series().diff().dt.days.fillna(0)
    group['mask'] = group['제곱미터당_실거래가'].notnull().astype(float)
    group['제곱미터당_실거래가_filled'] = group['제곱미터당_실거래가'].ffill().bfill()
    group['제곱미터당_실거래가_norm'] = scaler.transform(group[['제곱미터당_실거래가_filled']])
    values = group[['제곱미터당_실거래가_norm', 'mask', 'dt']].values.astype(np.float32)
    if len(values) < seq_len:
        raise ValueError("시계열 길이가 부족합니다.")
    x_seq = values[-seq_len:]
    future_dates = []
    future_prices_per_sqm = []
    last_date = group.index[-1]
    current_date = last_date + pd.Timedelta(days=1)
    end_date = pd.Timestamp(f"{target_year}-12-31")
    region_dong_idx_tensor = torch.tensor([region_dong_idx], dtype=torch.long, device=device)
    mean_area = group['전용면적'].mean()
    model.eval()
    with torch.no_grad():
        while current_date <= end_date:
            x_tensor = torch.from_numpy(x_seq).unsqueeze(0).float().to(device)
            pred_norm = model(x_tensor, region_dong_idx_tensor).cpu().numpy()[0][0]
            pred_price_per_sqm = scaler.inverse_transform([[pred_norm]])[0][0]
            if current_date.year == target_year:
                future_dates.append(str(current_date.date()))
                future_prices_per_sqm.append(float(pred_price_per_sqm))
            x_seq = np.vstack([x_seq[1:], np.array([pred_norm, 1.0, 1.0])])
            current_date += pd.Timedelta(days=1)
    return future_dates, future_prices_per_sqm

# 6. 예측 엔드포인트 (제곱미터당 실거래가 반환)
@app.post("/predict", response_model=PredictResponse)
def predict(request: PredictRequest):
    try:
        dates, prices_per_sqm = predict_year_price(
            model=model,
            region_dong_name=request.region_dong_name,
            target_year=request.target_year,
            df_grouped=df_grouped,
            scaler=scaler,
            region_dong_to_idx=region_dong_to_idx,
            seq_len=30,
            device=device
        )
        mean_price = np.mean(prices_per_sqm) if prices_per_sqm else 0.0
        return PredictResponse(
            region_dong_name=request.region_dong_name,
            target_year=request.target_year,
            predicted_prices=prices_per_sqm,
            dates=dates,
            mean_price=mean_price
        )
    except Exception as e:
        print("예측 중 에러:", e)
        raise HTTPException(status_code=400, detail=str(e))


# 7. 실제 연도별 제곱미터당 평균 실거래가 반환 엔드포인트
@app.post("/actual_price_per_sqm", response_model=ActualPricePerSqmResponse)
def get_actual_price_per_sqm(request: ActualPricePerSqmRequest):
    group = df_grouped[df_grouped['지역동'] == request.region_dong_name]
    if group.empty:
        raise HTTPException(status_code=404, detail="해당 지역동의 데이터가 없습니다.")
    year_means = group.groupby('연도')['제곱미터당_실거래가'].mean()
    return ActualPricePerSqmResponse(
        years=list(year_means.index.astype(int)),
        mean_price_per_sqm=list(year_means.values)
    )

# 8. 헬스 체크 엔드포인트
@app.get("/")
def root():
    return {"message": "Korean Real Estate Price Prediction API is running."}
