package com.teamdeco.websockettrade.presentation.coinlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.teamdeco.coinlist.entity.PriceType
import com.teamdeco.coinlist.entity.SortType
import com.teamdeco.websockettrade.databinding.ActivityCoinListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinList : AppCompatActivity() {

    private lateinit var binding: ActivityCoinListBinding
    private val viewmodel : CoinViewModel by viewModels()
    private lateinit var coinListAdapter : CoinAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()
        initListener()

    }


    fun setAdapter() {
        coinListAdapter = CoinAdapter()
        binding.coinListRecyclerView.adapter = coinListAdapter

        // 실시간으로 업데이트 된 데이터 가져옴
        collectNewCoinData()
    }


    fun initListener() {

        // 현재가 정렬
        binding.sortCurrent.setOnClickListener {
            // 현재 정렬 상태 저장
            when(viewmodel.stateSortType.value) {
                SortType.DEFAULT -> {
                    coinListAdapter.sortCoinList(PriceType.CURRENT, SortType.DOWN)
                    viewmodel._stateSortType.value = SortType.DOWN
                }

                SortType.DOWN -> {
                    coinListAdapter.sortCoinList(PriceType.CURRENT, SortType.UP)
                    viewmodel._stateSortType.value = SortType.UP
                }

                SortType.UP -> {
                    coinListAdapter.sortCoinList(PriceType.CURRENT, SortType.DEFAULT)
                    viewmodel._stateSortType.value = SortType.DEFAULT
                }
            }
            viewmodel._statePriceType.value = PriceType.CURRENT
        }


        // 거래대금 정렬
        binding.sortAccum.setOnClickListener {
            // 현재 정렬 상태 저장
            when(viewmodel.stateSortType.value) {
                SortType.DEFAULT -> {
                    coinListAdapter.sortCoinList(PriceType.ACC_TRADE_24, SortType.DOWN)
                    viewmodel._stateSortType.value = SortType.DOWN
                }

                SortType.DOWN -> {
                    coinListAdapter.sortCoinList(PriceType.ACC_TRADE_24, SortType.UP)
                    viewmodel._stateSortType.value = SortType.UP
                }

                SortType.UP -> {
                    coinListAdapter.sortCoinList(PriceType.ACC_TRADE_24, SortType.DEFAULT)
                    viewmodel._stateSortType.value = SortType.DEFAULT
                }
            }
            viewmodel._statePriceType.value = PriceType.ACC_TRADE_24
        }
    }

    fun collectNewCoinData() {
        lifecycleScope.launch {
            viewmodel.coinData.collect { coins ->
                // 새롭게 들어온 데이터를 업데이트
                coinListAdapter.setNewData(coins)
                coinListAdapter.sortCoinList(viewmodel.statePriceType.value,
                    viewmodel.stateSortType.value)
            }
        }
        viewmodel.startGetCoinData() // 웹소켓 연결, 데이터 호출 시작
    }

    override fun onDestroy() {
        super.onDestroy()
        viewmodel.stopGetCoinData()
    }

}