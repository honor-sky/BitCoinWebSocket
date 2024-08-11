package com.teamdeco.websockettrade.domain.usecase.coin

import com.teamdeco.domain.entity.Ticker
import com.teamdeco.websockettrade.domain.repository.upbit_coin.CoinRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

/* 사용자가 실제로 앱을 사용하면서 사용할 기능들을 중심으로 구성하고 있습니다.
 * UseCase 명칭만으로도 앱의 기능들을 한 눈에 파악할 수 있도록 하는 것이 목적입니다.
 * 하나의 UseCase에서 함수를 사용해 좀 더 세부적인 기능을 나누고 있습니다.
 */

class GetCoinInfo @Inject constructor (
    private val coinRepository: CoinRepository
) {

    fun startGetCoinData() {
        coinRepository.connectUpbitWebSocket()
    }

    fun stopGetCoinData() {
        coinRepository.disconnectUpbitWebSocket()
    }

    fun getTickerData(): Flow<Ticker> {
        return coinRepository.getTickerData()
    }

    /* 다른 코인 데이터를 가져올 경우 여기 추가 */
    /* 다른 플랫폼의 코인 데이터를 가져올 경우 여기 추가 */

}