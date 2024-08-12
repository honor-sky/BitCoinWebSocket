## Intro
- Upbit의 웹소켓을 사용해 비트코인의 현재가, 누적대금 정보를 실시간으로 화면에 표시 합니다
</br>
<img src="https://github.com/user-attachments/assets/3ecc0091-6b69-4462-8331-57271850bbb3" width="280" height="560">


## 🗺️ Information & Design

### 💡 View Design
- 화면은 단일 액티비티로 구성 / XML을 사용해 UI 구성
- 리사이클러뷰를 사용해 리스트 화면 구성 & 어댑터를 통해 리스트 정보를 저장하고 아이템 디자인 반영

</br>

### 💡 Feature Design
1. **비트코인 정보 조회 기능**
   - 주어진 20개의 비트코인 종목을 리스트에 표시 
   - 실시간으로 들어오는 코인 데이터의 현재가/거래대금 데이터에 변화가 있을 경우, 해당 종목의 값 변경 
   - 실시간으로 들어오는 코인 데이터의 현재가/거래대금 데이터의 상승(빨강)/하락(파랑)에 따라 색깔 박스 애니메이션 1초간 적용
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
  
   </br> </br>
   현재는 Ticker(현재가) 데이터만 사용하고 있지만 추후에 업비트 웹소켓에서 제공하는 다른 데이터(Trade, OrderBook) 도 사용할 가능성에 대해 생각해보았습니다. 

   그래서 좀 더 확장 가능한 형태로 코드를 짜고 싶다고 생각했고 처음에는 웹소켓 인터페이스를 만들고 어떤 데이터를 가져오느냐에 따라 서로 다른 구현체를 만들어 사용하는 방식으로 구현하는 방식을 고민했으나 
   Request/Response 타입이 달라진다는 것 외에는 구현체마다 차이가 크지는 않을 것 같다고 생각했습니다. 

   따라서 동일한 플랫폼(업비트)에서 사용하는 웹소켓의 경우, 
   1개의 웹소켓 매니저를 object 타입으로 만들고 reqeust 형태를 다르게 구성할 수 있는 create000() 함수와 
   onMessage() 에서 when을 사용해 다양한 Response 타입을 처리할 수 있도록 설계하고자 했습니다.



     ```
     @Singleton
     object UpbitWebSocketManager {

              private val UPBIT_WEBSOCKET_BASE_URL = "https://api.upbit.com/websocket/v1"
    
              private val uuid = UUID.randomUUID().toString()
              private val gson: Gson = Gson()
              private var websocket: WebSocket? = null
    
              private val _tickerMessagesFlow = MutableSharedFlow<TickerResponse>()
    
              val dispatcher = Dispatcher().apply {
                   maxRequestsPerHost = 1
              } 

             object OkHttpClientSingleton {
                   val client: OkHttpClient = OkHttpClient.Builder()
                       .dispatcher(dispatcher)
                       .build()
            }

             object RequestSingleton {
                   val request: Request = Request.Builder()
                       .url(UPBIT_WEBSOCKET_BASE_URL)
                       .build()
             }

             fun connect() {
                   websocket = OkHttpClientSingleton.client.newWebSocket(RequestSingleton.request, webSocketListener)
             }

             fun disconnect() {
                   websocket?.close(1000, "THE END")
             } 


             private fun createTicket() : String {
                  val ticket = Ticket(uuid)
                  val type = Type(DataType.ticker, CodeList.codeList)
                  return gson.toJson(arrayListOf(ticket, type))
             }
             /* 다른 데이터를 요청하는 경우, 해당 함수를 구현해 Request구성을 다르게 함
             * private fun createTrade() : String
             * private fun createOrderBook() : String
             */


             fun observeTickerMessages(): Flow<TickerResponse> = _tickerMessagesFlow.asSharedFlow()


             private val webSocketListener = object : WebSocketListener() {

             // 웹소켓이 열리면 호출
             override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
                     super.onOpen(webSocket, response)

                     webSocket.send(createTicket())
             }

             // 웹소켓으로부터 데이터를 받을 때 호출
             override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                     super.onMessage(webSocket, bytes)

                    val bytesToString = bytes.toByteArray().decodeToString()
                    val jsonObject = JSONObject(bytesToString)
                    val type = jsonObject.getString("type")

                   when (type) {
                       DataType.ticker.toString() -> {
                            val data = gson.fromJson(bytesToString, TickerResponse::class.java)
                            CoroutineScope(Dispatchers.IO).launch {
                               _tickerMessagesFlow.emit(data)
                            }
                       }

                    /* 
                      // 다른 데이터를 요청하는 경우 
                      DataType.trade.toString() -> {}
                      DataType.orderbook.toString() -> {}
                      DataType.mytrade.toString() -> {}
                    */
                 }
             }

             // 웹소켓이 닫힐 때 호출
             override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                     super.onClosed(webSocket, code, reason)
                     websocket = null
             }
          }

        }

     ```




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
