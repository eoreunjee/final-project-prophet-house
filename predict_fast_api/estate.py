import torch

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# 1. kaggle 라이브러리 설치
!pip install kaggle

# 2. kaggle.json 업로드
from google.colab import files
files.upload()  # kaggle.json 파일 업로드

# 3. kaggle.json을 올바른 위치로 이동
!mkdir -p ~/.kaggle
!cp kaggle.json ~/.kaggle/
!chmod 600 ~/.kaggle/kaggle.json

# 4. 데이터셋 다운로드
!kaggle datasets download -d brainer3220/korean-real-estate-transaction-data

# 5. 압축 해제
import zipfile
with zipfile.ZipFile("korean-real-estate-transaction-data.zip", "r") as zip_ref:
    zip_ref.extractall("data")

# 6. 데이터 불러오기
import pandas as pd
df = pd.read_csv("data/Apart Deal.csv")
print(len(df))
print(df.head())

import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import torch
from torch.utils.data import Dataset, DataLoader
import torch.nn as nn
import joblib
import pickle
import matplotlib.pyplot as plt

# 1. 데이터 불러오기 및 기본 전처리
df = pd.read_csv("data/Apart Deal.csv", low_memory=False)
df['거래일'] = pd.to_datetime(df['거래일'], errors='coerce')
df['거래금액'] = df['거래금액'].astype(str).str.replace(',', '').astype(float)
df['전용면적'] = df['전용면적'].astype(float)

# 전용면적 결측치, 0, 음수 제거
df = df[df['전용면적'] > 0]
df = df.dropna(subset=['거래일', '거래금액', '전용면적', '지번', '지역코드', '법정동'])

# 2. 파생변수 생성
df['제곱미터당_실거래가'] = df['거래금액'] / df['전용면적']
df['지역코드'] = df['지역코드'].astype(str).str.split('.').str[0]
df = df.sort_values(['지역코드', '법정동', '거래일'])
df['지역동'] = df['지역코드'].astype(str) + '_' + df['법정동'].astype(str)
df['연도'] = df['거래일'].dt.year

# 3. 연도별 평균값 딕셔너리 생성
yearly_mean_price = df.groupby(['지역동', '연도'])['거래금액'].mean().to_dict()
yearly_mean_price_per_sqm = df.groupby(['지역동', '연도'])['제곱미터당_실거래가'].mean().to_dict()

# 4. 결측치 보완 (map 사용)
df['거래금액_filled'] = df['거래금액'].fillna(
    df.apply(lambda row: yearly_mean_price.get((row['지역동'], row['연도']), np.nan), axis=1)
)
df['제곱미터당_실거래가_filled'] = df['제곱미터당_실거래가'].fillna(
    df.apply(lambda row: yearly_mean_price_per_sqm.get((row['지역동'], row['연도']), np.nan), axis=1)
)

# 추가로 남은 결측치는 ffill/bfill로 보완
df['거래금액_filled'] = df.groupby('지역동')['거래금액_filled'].ffill().bfill()
df['제곱미터당_실거래가_filled'] = df.groupby('지역동')['제곱미터당_실거래가_filled'].ffill().bfill()

# 5. 거래일별 평균 집계
df_grouped = df.groupby(['지역동', '거래일']).agg({
    '거래금액_filled': 'mean',
    '제곱미터당_실거래가_filled': 'mean',
    '전용면적': 'mean'
}).reset_index()
df_grouped['연도'] = df_grouped['거래일'].dt.year

# 6. 정규화 (제곱미터당 실거래가 기준)
scaler = MinMaxScaler()
scaler.fit(df_grouped['제곱미터당_실거래가_filled'].values.reshape(-1, 1))

# 7. 시계열 샘플 생성
region_dong_names = df_grouped['지역동'].unique()
region_dong_to_idx = {name: i for i, name in enumerate(region_dong_names)}
seq_len = 30
data = []

for region_dong, group in df_grouped.groupby('지역동'):
    group = group.set_index('거래일').asfreq('D')
    group['dt'] = group.index.to_series().diff().dt.days.fillna(0)
    group['mask'] = group['거래금액_filled'].notnull().astype(float)
    group['연도'] = group.index.year

    # 결측치 보강(혹시 남아있을 경우)
    group['거래금액_filled'] = group['거래금액_filled'].ffill().bfill()
    group['제곱미터당_실거래가_filled'] = group['제곱미터당_실거래가_filled'].ffill().bfill()

    group['제곱미터당_실거래가_norm'] = scaler.transform(group[['제곱미터당_실거래가_filled']])
    values = group[['제곱미터당_실거래가_norm', 'mask', 'dt']].values.astype(np.float32)
    region_dong_idx = region_dong_to_idx[region_dong]
    for i in range(len(group) - seq_len):
        x = values[i:i+seq_len]
        y = values[i+seq_len][0]
        data.append((x, region_dong_idx, y))

# 8. 결측치 최종 점검
print(df_grouped.isnull().sum())

# 6. PyTorch Dataset 클래스
class MultiRegionDongTimeSeriesDataset(Dataset):
    def __init__(self, data):
        self.data = data
    def __len__(self):
        return len(self.data)
    def __getitem__(self, idx):
        x, region_dong_idx, y = self.data[idx]
        x = np.array(x, dtype=np.float32)
        y = np.array([y], dtype=np.float32)
        return torch.from_numpy(x), int(region_dong_idx), torch.from_numpy(y)

# 7. 모델 정의
input_dim = 3
emb_dim = 8
hidden_dim = 32

class MultiRegionDongGRUD(nn.Module):
    def __init__(self, input_dim, hidden_dim, num_region_dong, emb_dim=8):
        super().__init__()
        self.region_dong_emb = nn.Embedding(num_region_dong, emb_dim)
        self.gru = nn.GRU(input_dim + emb_dim, hidden_dim, batch_first=True)
        self.fc = nn.Sequential(nn.Linear(hidden_dim, 1),nn.ReLU())
    def forward(self, x, region_dong_idx):
        region_dong_emb = self.region_dong_emb(region_dong_idx).unsqueeze(1)
        region_dong_emb = region_dong_emb.expand(-1, x.size(1), -1)
        x_cat = torch.cat([x, region_dong_emb], dim=2)
        out, _ = self.gru(x_cat)
        out = self.fc(out[:, -1, :])
        return out


# 8. Dataset 생성
dataset = MultiRegionDongTimeSeriesDataset(data)

# 9. 학습/테스트 분리 및 DataLoader 생성
train_size = int(len(dataset) * 0.8)
test_size = len(dataset) - train_size
train_dataset, test_dataset = torch.utils.data.random_split(dataset, [train_size, test_size])

train_loader = DataLoader(train_dataset, batch_size=256, shuffle=True) #128
test_loader = DataLoader(test_dataset, batch_size=256, shuffle=False) #128

# 10. 모델 초기화
num_region_dong = len(region_dong_names)
model = MultiRegionDongGRUD(
    input_dim=input_dim,
    hidden_dim=hidden_dim,
    num_region_dong=num_region_dong,
    emb_dim=emb_dim
)
criterion = nn.MSELoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

# 11. GPU 사용 가능 여부 및 device 설정
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(f"Using device: {device}")
model = model.to(device)

# 12. 학습 루프
for epoch in range(10):
    model.train()
    total_loss = 0
    for x, region_dong_idx, y in train_loader:
        x = x.to(device)
        y = y.to(device)
        region_dong_idx = region_dong_idx.clone().detach().to(dtype=torch.long, device=device)
        pred = model(x, region_dong_idx)
        loss = criterion(pred, y)
        optimizer.zero_grad()
        loss.backward()
        optimizer.step()
        total_loss += loss.item()
    print(f'Epoch {epoch+1}, Train Loss: {total_loss/len(train_loader):.4f}')

# 6. PyTorch Dataset 클래스
class MultiRegionDongTimeSeriesDataset(Dataset):
    def __init__(self, data):
        self.data = data
    def __len__(self):
        return len(self.data)
    def __getitem__(self, idx):
        x, region_dong_idx, y = self.data[idx]
        x = np.array(x, dtype=np.float32)
        y = np.array([y], dtype=np.float32)
        return torch.from_numpy(x), int(region_dong_idx), torch.from_numpy(y)

# 7. 모델 정의
input_dim = 3
emb_dim = 8
hidden_dim = 32

# 13. 테스트 및 평가
y_true_list = []
y_pred_list = []
total_test_loss = 0
model.eval()
with torch.no_grad():
    for x, region_dong_idx, y in test_loader:
        x = x.to(device)
        y = y.to(device)
        region_dong_idx = region_dong_idx.clone().detach().to(dtype=torch.long, device=device)
        pred = model(x, region_dong_idx)
        loss = criterion(pred, y)
        total_test_loss += loss.item()
        y_true_list.extend(y.cpu().numpy().flatten())
        y_pred_list.extend(pred.cpu().numpy().flatten())

avg_test_loss = total_test_loss / len(test_loader)
print(f'Test Loss: {avg_test_loss:.4f}')

y_true = np.array(y_true_list)
y_pred = np.array(y_pred_list)
ss_res = np.sum((y_true - y_pred) ** 2)
ss_tot = np.sum((y_true - np.mean(y_true)) ** 2)
r_squared = 1 - (ss_res / ss_tot)
r_squared_percentage = r_squared * 100
print(f"R-squared: {r_squared:.4f}")
print(f"R-squared(%): {r_squared_percentage:.2f}%")

from sklearn.metrics.pairwise import cosine_similarity

# y_true, y_pred는 1차원 numpy 배열
y_true_reshaped = y_true.reshape(1, -1)
y_pred_reshaped = y_pred.reshape(1, -1)
cos_sim = cosine_similarity(y_true_reshaped, y_pred_reshaped)[0][0]

print(f"Cosine Similarity: {cos_sim:.4f}")
print(f"Cosine Similarity(%): {cos_sim * 100:.2f}%")

# 14. 산점도 그래프
plt.figure(figsize=(8, 8))
plt.scatter(y_true_list, y_pred_list, alpha=0.6)
plt.plot([min(y_true_list), max(y_true_list)], [min(y_true_list), max(y_true_list)], 'r--')
plt.xlabel('True Value')
plt.ylabel('Predicted Value')
plt.title('True vs Predicted')
plt.show()


# 15. Google Drive 저장 (Colab 환경)
from google.colab import drive
drive.flush_and_unmount()
drive.mount('/content/drive')
drive_path = '/content/drive/MyDrive/'
torch.save(model.state_dict(), drive_path + 'model.pth')
joblib.dump(scaler, drive_path + 'scaler.pkl')
with open(drive_path + 'region_dong_to_idx.pkl', 'wb') as f:
    pickle.dump(region_dong_to_idx, f)
with open(drive_path + 'region_dong_names.pkl', 'wb') as f:
    pickle.dump(region_dong_names, f)
with open(drive_path + 'df_grouped.pkl', 'wb') as f:
    pickle.dump(df_grouped, f)
print("Google Drive에 저장 완료: model.pth, scaler.pkl, region_dong_to_idx.pkl, region_dong_names.pkl, df_grouped.pkl")

# 16. 예측 함수 (지역동 단위)
def predict_year_price(model, region_dong_name, target_year, df_grouped, scaler, region_dong_to_idx, seq_len=30, device='cpu'):
    if region_dong_name not in region_dong_to_idx:
        raise ValueError("존재하지 않는 지역동명입니다.")
    region_dong_idx = region_dong_to_idx[region_dong_name]
    group = df_grouped[df_grouped['지역동'] == region_dong_name].copy()
    # 1. 거래일별로 숫자 컬럼만 평균 집계 (컬럼명 맞추기)
    group = group.groupby('거래일').agg({
        '거래금액_filled': 'mean',
        '전용면적': 'mean',
        '제곱미터당_실거래가_filled': 'mean'
    }).reset_index()
    # 2. asfreq('D')로 일 단위 시계열 변환
    group = group.set_index('거래일').asfreq('D')
    # 이하 동일
    group['dt'] = group.index.to_series().diff().dt.days.fillna(0)
    group['mask'] = group['거래금액_filled'].notnull().astype(float)
    # 결측치 보완 (혹시 남아있다면)
    group['제곱미터당_실거래가_filled'] = group['제곱미터당_실거래가_filled'].ffill().bfill()
    # 정규화 (제곱미터당 실거래가 기준)
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
            # 역정규화는 반드시 제곱미터당 실거래가 기준
            pred_price_per_sqm = scaler.inverse_transform([[pred_norm]])[0][0]
            if current_date.year == target_year:
                future_dates.append(current_date)
                future_prices_per_sqm.append(pred_price_per_sqm)
            x_seq = np.vstack([x_seq[1:], np.array([pred_norm, 1.0, 1.0])])
            current_date += pd.Timedelta(days=1)
    return pd.DataFrame({'date': future_dates, 'predicted_price_per_sqm': future_prices_per_sqm})




    # 예측 예시
# https://www.kaggle.com/datasets/brainer3220/korean-real-estate-transaction-data
region_dong_name = '11170_한남동' #'11320_방학동'  # '11170_한남동'
target_year = 2026
future_df = predict_year_price(
    model, region_dong_name, target_year, df_grouped, scaler, region_dong_to_idx, seq_len=30, device=device
)
print(future_df)
print(f"{target_year}년 {region_dong_name} 예측 평균 거래금액: {future_df['predicted_price_per_sqm'].mean():,.0f}(만원/m²)")
