## Intro
- Upbit의 웹소켓을 사용해 비트코인의 현재가, 누적대금 정보를 실시간으로 화면에 표시 합니다



## 🗺️ Information & Design

### Feature Design
1. **비트코인 정보 조회 기능**
   - 주어진 20개의 비트코인 종목을 리스트에 표시 
   - 실시간으로 들어오는 코인 데이터의 현재가/거래대금 데이터에 변화가 있을 경우, 해당 종목의 값 변경 
   - 실시간으로 들어오는 코인 데이터의 현재가/거래대금 데이터의 상승(빨강)/하락(파랑)에 따라 색깔 박스 애니메이션 2초간 적용
   - 전일 대비 상승/하락/보합 결과에 따라 현재가 색 빨강(상승)/파랑(하양) 으로 설정
   - 정렬 기능

2. **정렬 기능**
   - 현재가 / 누적 거래대금 기준으로 정렬 가능
   - 디폴트(처음 표시한 순서대로) / 내림차순 / 올림차순 으로 정렬 가능


### Architecture Design
1. **Clean Architecture + MVVM**
   - 
3. **WebSocket**
   - 제공된 sudo 코드와 업비트 문서를 참고해서 구현
   - client, request는 싱글톤으로 생성
   - 



## Foldering
```
├── common
│   ├── CodeList
│   └── DataType 
│
├── data 
│   ├── datasource (소스 별로 실제 데이터 호출을 정의)
│   │    └── upbit_websocket
│   │          └── UpbitWebSocketManager
│   │
│   ├── repository 
│   │     └── upbit_coin 
│   │          └──CoinRepositoryImpl
│   ├── di
│   │    ├── WebSocketModule
│   │    └── RepositoryModule 
│   │
│   ├── mapper
│   │     └── WebSocketMapper
│   │
│   └── entity
│         └── auth
│              ├── TickerResponse
│              ├── Ticket
│              └── Type
│
├── domain (순수 java, kotlin 계층)
│   ├── repository 
│   │     └── upbit_coin 
│   │          └──CoinRepository
│   ├── entity 
│   │     └── Ticker
│   └── usecase 
│         └── coin 
│              └── GetCoinData
│
│
└── presentation 
│.  ├── entity (presentation 계층에서 사용하는 데이터 정의)
│   └── coinList 
│       ├── CoinList 
│       ├── CoinAdapter
│       └── CoinViewModel
│
├── WebSocketApplication
```
