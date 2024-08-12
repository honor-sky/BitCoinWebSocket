package com.teamdeco.websockettrade.data.mapper

import com.teamdeco.domain.entity.Ticker
import com.teamdeco.websockettrade.data.model.TickerResponse


fun TickerResponse.toTicker() : Ticker {
    return Ticker(
     code = code,
        pre_trade_price = trade_price,
     trade_price = trade_price,
     acc_trade_price_24h = acc_trade_price_24h,
     change = change
    )
}