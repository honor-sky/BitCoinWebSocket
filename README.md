## Intro
- Upbit의 웹소켓을 사용해 비트코인의 현재가, 누적대금 정보를 실시간으로 화면에 표시 합니다
</br>
<img src="https://github.com/user-attachments/assets/048be972-aae6-40e6-9741-f02b812af9ce" width="280" height="560">



## 🗺️ Information & Design

### 💡 Feature Design
1. **비트코인 정보 조회 기능**
   - 주어진 20개의 비트코인 종목을 리스트에 표시 
   - 실시간으로 들어오는 코인 데이터의 현재가/거래대금 데이터에 변화가 있을 경우, 해당 종목의 값 변경 
   - 실시간으로 들어오는 코인 데이터의 현재가/거래대금 데이터의 상승(빨강)/하락(파랑)에 따라 색깔 박스 애니메이션 2초간 적용
   - 전일 대비 상승/하락/보합 결과에 따라 현재가 색 빨강(상승)/파랑(하양) 으로 설정
   - 정렬 기능 

2. **정렬 기능**
   - 현재가 / 누적 거래대금 기준으로 정렬 가능
   - 디폴트(처음 표시한 순서대로) / 내림차순 / 올림차순 으로 정렬 가능

</br>

### 💡 Architecture Design

1. **Clean Architecture + MVVM**
   - 싱글 모듈로 presentation, domain, data 3개의 계층 으로 구성
     - domain 계층은 사용자가 실제 앱에서 사용할 기능을 위주로 UseCase 구성
     - data 계층은 데이터 소스를 관리하는 datasource 폴더를 만들고 websocket 코드를 위치 / repositorys
   - MVVM 패턴을 사용해 ViewModel에 정렬 상태, 실시간으로 들어오는 코인 정보를 저장하고 관리
     - 정렬 상테는 계속 유지되어야 하므로 StateFlow, 코인 정보는 실시간으로 계속 변동되므로 SharedFlow 를 사용

2. **WebSocket**
   - 제공된 sudo 코드와 업비트 문서를 참고해서 구현
   - client, request는 싱글톤으로 생성 / 동일한 요청이 중복되는 것을 방지하기 위해 dispatcher를 사용해 효율적으로 네트워크 관리
   - 웹소켓 연결, 해제를 위한 함수를 만들고 외부에서 직접 연결, 해제를 요청할 수 있도록 구현
   - 코인 데이터는 실시간 스트림 데이터에 적합한 것으로 알려져 있는 Flow를 사용
   

</br>

### 💡 View Design
- 화면은 단일 액티비티로 구성 / XML을 사용해 UI 구성
- 리사이클러뷰를 사용해 리스트 화면 구성 & 어댑터를 통해 리스트 정보를 저장하고 아이템 디자인 반영

</br>

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
