package com.teamdeco.domain.entity

data class Ticker(
    val code: String,
    var trade_price : Double,
    var acc_trade_price_24h : Double,
    var change : String
)

