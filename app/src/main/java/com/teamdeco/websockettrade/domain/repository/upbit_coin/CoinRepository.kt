package com.teamdeco.websockettrade.domain.repository.upbit_coin

import com.teamdeco.domain.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface CoinRepository{

    fun connectUpbitWebSocket()
    fun disconnectUpbitWebSocket()
    fun getTickerData() : Flow<Ticker>

}