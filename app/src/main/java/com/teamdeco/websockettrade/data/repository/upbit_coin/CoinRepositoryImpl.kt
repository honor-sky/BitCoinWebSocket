package com.teamdeco.websockettrade.data.repository.upbit_coin

import com.teamdeco.domain.entity.Ticker
import com.teamdeco.websockettrade.data.datasource.upbit_websocket.UpbitWebSocketManager
import com.teamdeco.websockettrade.data.mapper.*
import com.teamdeco.websockettrade.domain.repository.upbit_coin.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/*
* 데이터를 실제 앱에서 사용하는 형태로 가공합니다.
* 사용자가 실제로 사용할 데이터, 화면에 표시하기 위해 필요한 데이터를 중점적으로 생각합니다.
* */
class CoinRepositoryImpl @Inject constructor(): CoinRepository {

    override fun connectUpbitWebSocket() {
        UpbitWebSocketManager.connect()
    }

    override fun disconnectUpbitWebSocket() {
        UpbitWebSocketManager.disconnect()
    }

    override fun getTickerData() : Flow<Ticker> {
        return UpbitWebSocketManager.observeTickerMessages().map{it.toTicker()}
    }

    /*
    fun getTradeData() : Flow<Trade> {
        return UpbitWebSocketManager.observeTradeMessages().map{}
    }
     */


}