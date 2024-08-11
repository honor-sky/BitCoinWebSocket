package com.teamdeco.coinlist.entity

enum class CoinType(val coinAbb: String, val coinName: String) {
    SAND("KRW-SAND", "샌드박스"),
    BTC("KRW-BTC", "비트코인"),
    XRP("KRW-XRP", "리플"),
    SOL("KRW-SOL", "솔라나"),
    ETH("KRW-ETH", "이더리움"),
    SHIB("KRW-SHIB", "시바이누"),
    SEI("KRW-SEI", "세이 네트워크"),
    NEAR("KRW-NEAR", "니어 프로토콜"),
    ID("KRW-ID", "스페이스 아이디"),
    AVAX("KRW-AVAX", "아발란체"),
    DOGE("KRW-DOGE", "도지코인"),
    SUI("KRW-SUI", "수이"),
    ETC("KRW-ETC", "이더리움 클래식"),
    BTG("KRW-BTG", "비트코인 골드"),
    CTC("KRW-CTC", "크레딧코인"),
    ASTR("KRW-ASTR", "아스타"),
    MINA("KRW-MINA", "미나 프로토콜"),
    SC("KRW-SC", "시아코인"),
    ZRX("KRW-ZRX", "제로엑스"),
    WAVES("KRW-WAVES", "웨이브");

    companion object {
        fun fromKrwAbbr(abb: String): String? {
            return values().find { it.coinAbb == abb }?.coinName
        }
    }
}