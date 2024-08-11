package com.teamdeco.websockettrade.data.datasource.upbit_websocket

import android.util.Log
import com.google.gson.Gson
import com.teamdeco.data.entity.Ticket
import com.teamdeco.data.entity.Type
import com.teamdeco.websockettrade.common.CodeList
import com.teamdeco.websockettrade.common.DataType
import com.teamdeco.websockettrade.data.model.TickerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Dispatcher
import okio.ByteString
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit.*
import javax.inject.Singleton


@Singleton
object UpbitWebSocketManager {

    /**
     * 팩토리 패턴을 사용해 ticker, trade, orderbook, mytrade에 따라 웹소켓 인스턴스 객체를 따로 만들수도 있지만
     * 동시에 ticker, trade, orderbook, mytrade 데이터를 모두 사용하거나 필요로 하는 경우도 있을 수 있는데
     * 굳이 인스턴스 객체를 따로따로 나눠서 만들 필요까지는 없다고 생각함.
     * 아예 완전히 다른 플랫폼의 웹소켓을 사용한다면 모르겠지만.... reqeust, response 구조만 맞춰주면 되는거라고 생각했기 때문에 이렇게 구현
     */


    private val UPBIT_WEBSOCKET_BASE_URL = "https://api.upbit.com/websocket/v1"
    private val uuid = UUID.randomUUID().toString()
    private val gson: Gson = Gson()
    private var websocket: WebSocket? = null
    private val _tickerMessagesFlow = MutableSharedFlow<TickerResponse>()
    //private val _tradeMessagesFlow = MutableSharedFlow<TradeResponse>()
    //private val _orderbookMessagesFlow = MutableSharedFlow<OrderBookResponse>()

    val dispatcher = Dispatcher().apply {
        maxRequestsPerHost = 1
    }

    object OkHttpClientSingleton {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, SECONDS)
            .writeTimeout(30, SECONDS)
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
    // private fun createTrade() : String
    // private fun createOrderBook() : String


    fun observeTickerMessages(): Flow<TickerResponse> = _tickerMessagesFlow.asSharedFlow()
    //fun observeMessages(): Flow<TradeResponse> = _tradeMessagesFlow.asSharedFlow()
    //fun observeMessages(): Flow<OrderBookResponse> = _orderbookMessagesFlow.asSharedFlow()



    private val webSocketListener = object : WebSocketListener() {

        // 웹소켓이 열리면 호출
        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.d("webSocketListener","onOpen")
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
                    //Log.d("webSocketListener","onMessage : ${data}")
                    CoroutineScope(Dispatchers.IO).launch {
                        _tickerMessagesFlow.emit(data)
                    }
                }

                /* 다른 데이터를 요청하는 경우 */
                DataType.trade.toString() -> {}
                DataType.orderbook.toString() -> {}
                DataType.mytrade.toString() -> {}
            }
        }

        // 웹소켓이 닫힐 때 호출
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            websocket = null
        }
    }

}