package com.teamdeco.websockettrade.data.model

data class TickerResponse(
    val type: String,
    val code: String,
    val trade_price : Double,          // 현재가
    val acc_trade_price_24h : Double,  // 24시간 누적대금
    val change : String                // 전일 대비
)
