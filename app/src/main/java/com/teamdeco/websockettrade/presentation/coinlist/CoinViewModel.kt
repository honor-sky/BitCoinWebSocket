package com.teamdeco.websockettrade.presentation.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdeco.coinlist.entity.PriceType
import com.teamdeco.coinlist.entity.SortType
import com.teamdeco.domain.entity.Ticker
import com.teamdeco.domain.usecase.GetCoinInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor (
    private val getCoinInfo: GetCoinInfo
) : ViewModel() {

    // coin 데이터를 실시간으로 받아옴 (데이터를 실시간으로 받아와 사용 -> SharedFlow)
    private val _coinData = MutableSharedFlow<Ticker>()
    val coinData: SharedFlow<Ticker> get() = _coinData


    // 현재 정렬 상태 저장 (상태를 지속적으로 저장 -> StateFlow)
    var _statePriceType = MutableStateFlow<PriceType>(PriceType.CURRENT)
    val statePriceType: StateFlow<PriceType> = _statePriceType
    var _stateSortType = MutableStateFlow<SortType>(SortType.DEFAULT)
    val stateSortType: StateFlow<SortType> = _stateSortType


    init {
        observeCoinData()
    }


  fun startGetCoinData() {
        getCoinInfo.startGetCoinData()
    }

    fun stopGetCoinData() {
        getCoinInfo.stopGetCoinData()
    }

    private fun observeCoinData() {
        viewModelScope.launch {
            getCoinInfo.getTickerData().collect { ticker ->
                _coinData.emit(ticker) // 실시간으로 들어온 데이터 방출
            }
        }
    }

}