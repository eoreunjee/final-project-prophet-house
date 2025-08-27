# ProphetHouse 🏠

> AI 기반 부동산 가격 예측 서비스

SSAFY 대전 5반 관통 프로젝트 - 머신러닝을 활용한 스마트 부동산 플랫폼

## 📖 프로젝트 개요

ProphetHouse는 빅데이터와 AI 기술을 활용하여 부동산 가격을 예측하고, 사용자에게 신뢰할 수 있는 부동산 시장 정보를 제공하는 웹 서비스입니다.

## ✨ 주요 기능

- 🤖 **AI 가격 예측**: 머신러닝 모델 기반 부동산 가격 예측
- 📊 **시장 분석**: 지역별 부동산 시장 동향 분석
- 🔍 **매물 검색**: 다양한 조건의 부동산 검색 및 필터링
- 📈 **트렌드 분석**: 시계열 데이터 기반 가격 변동 추이
- 💡 **투자 인사이트**: 데이터 기반 투자 가이드 제공

## 🛠️ 기술 스택

### Frontend
- Vue.js
- JavaScript
- Bootstrap

### Backend  
- Spring Boot
- Java
- MySQL

### AI/Data
- Python
- TensorFlow/Scikit-learn
- Pandas, NumPy

### Tools
- Git/GitLab
- Docker

## 🏗️ 시스템 아키텍처

![시스템 구조도](./docs/ProphetHouse_다이어그램.png)

## 📊 데이터베이스 설계

![ERD](./docs/ProphetHouse_ERD.png)

## 🎬 프로젝트 시연

[![시연 영상](./docs/video_thumbnail.png)](./docs/Prophet_House_시연영상_자막_.mp4)

## 📂 프로젝트 구조
ProphetHouse/
├── frontend/ # React 프론트엔드
├── backend/ # Spring Boot 백엔드
├── ai-model/ # AI 예측 모델
├── database/ # DB 스키마 및 초기 데이터
└── docs/ # 프로젝트 문서
├── ProphetHouse_설계서.pdf
├── ProphetHouse_화면정의서.pdf
├── Prophethouse_명세서.pdf
├── ProphetHouse_ERD.png
├── ProphetHouse_다이어그램.png
├── ProphetHouse_Diagram.puml
└── Prophet_House_시연영상_자막_.mp4


## 🚀 Quick Start

### 1. 저장소 클론
git clone https://github.com/your-username/ProphetHouse.git
cd ProphetHouse


### 2. 데이터베이스 설정
MySQL 실행 후
mysql -u root -p < database/schema.sql


### 3. 백엔드 실행
cd frontend
npm install
npm start


## 📋 API 문서

### 주요 엔드포인트

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/properties` | 부동산 목록 조회 |
| GET | `/api/predict/{id}` | 가격 예측 요청 |
| POST | `/api/analysis` | 시장 분석 데이터 |
| GET | `/api/trends` | 트렌드 데이터 조회 |

## 🎯 핵심 알고리즘

- **예측 모델**: GRU(Gated Recurrent Unit) 기반 시계열 예측
- **데이터 전처리**: 이상치 제거 및 정규화
- **특성 엔지니어링**: 지역, 교통, 편의시설 등 다차원 특성 추출

## 📈 성능 지표

- **예측 정확도**: MAE 기준 85% 이상
- **응답 시간**: 평균 200ms 이하
- **데이터 커버리지**: 전국 주요 도시 90% 이상

## 👥 팀원

| 이름 | 역할 | GitHub |
|------|------|--------|
| 유승준 | Frontend, UI/UX | [@username1](https://github.com/username1) |
| 이은지 | Backend, AI Model | [@username2](https://github.com/username2) |

## 📚 관련 문서

- 📋 [프로젝트 발표 자료](./docs/250528_137_대전_5반_관통PJT_유승준_이은지.pptx)
- 📐 [시스템 설계서](./docs/ProphetHouse_설계서.pdf)
- 🎨 [화면 정의서](./docs/ProphetHouse_화면정의서.pdf)
- 📖 [프로젝트 명세서](./docs/Prophethouse_명세서.pdf)

## 🏆 수상 및 성과

- 🥇 SSAFY 대전캠퍼스 5반 관통 프로젝트 우수상
- 📊 사용자 만족도 4.5/5.0 달성

## 📱 스크린샷

| 메인 페이지 | 예측 결과 | 시장 분석 |
|-----------|---------|---------|
| ![메인](screenshot1.png) | ![예측](screenshot2.png) | ![분석](screenshot3.png) |

## 🔮 향후 계획

- [ ] 모바일 앱 개발
- [ ] 실시간 알림 서비스
- [ ] 투자 포트폴리오 기능
- [ ] 블록체인 기반 거래 시스템

## 🤝 Contributing

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

- 📧 Email: your.email@ssafy.com
- 🐱 GitHub: [ProphetHouse](https://github.com/your-username/ProphetHouse)
- 📝 Blog: [프로젝트 회고](https://your-blog.com/prophethouse)

---

⭐ **이 프로젝트가 도움이 되었다면 Star를 눌러주세요!**
