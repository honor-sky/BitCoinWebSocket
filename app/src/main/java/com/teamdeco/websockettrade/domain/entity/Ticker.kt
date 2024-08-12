package com.teamdeco.domain.entity

data class Ticker(
    val code: String,
    var pre_trade_price : Double, // 이전 현재가를 저장
    var trade_price : Double,
    var acc_trade_price_24h : Double,
    var change : String
)

