## Intro
- 
- 식당을 직접 추가하거나 삭제할 수 있습니다.



## 🗺️ Information

### Design

#### Feature Design
1. 비트코인 정보 조회 기능
2. 정렬 기능

#### Architecture Design
1. Architecture Pattern : Clean Architecture + MVVM 




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
