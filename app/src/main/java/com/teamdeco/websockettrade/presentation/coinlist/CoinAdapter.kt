package com.teamdeco.websockettrade.presentation.coinlist

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamdeco.coinlist.entity.CoinType
import com.teamdeco.coinlist.entity.PriceType
import com.teamdeco.coinlist.entity.SortType
import com.teamdeco.domain.entity.Ticker
import com.teamdeco.websockettrade.R
import com.teamdeco.websockettrade.common.CodeList.codeList
import com.teamdeco.websockettrade.databinding.CoinItemBinding
import com.teamdeco.websockettrade.presentation.coinlist.entity.ChangeType


class CoinAdapter : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    var coinList : MutableList<Ticker> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        if(coinList != null) holder.bind(coinList[position])
    }

    override fun getItemCount() = coinList.size ?: 0


    fun setNewData(newCoinData : Ticker) {
        // coinList에서 동일한 CoinType 찾아서 값만 변경
        Log.d("webSocketListener","setNewData : ${newCoinData}")
        val data = coinList.firstOrNull() { it.code == newCoinData.code }
        if(data != null) {
            data.trade_price = newCoinData.trade_price
            data.acc_trade_price_24h = newCoinData.acc_trade_price_24h
            data.change = newCoinData.change

        } else {
            coinList.add(newCoinData)
        }

        notifyDataSetChanged()
    }

    fun sortCoinList(priceType : PriceType, sortType : SortType) {
        when(priceType) {
            PriceType.CURRENT -> {
                when(sortType) {
                    SortType.UP -> coinList.sortBy { it.trade_price }
                    SortType.DOWN -> coinList.sortByDescending { it.trade_price }
                    SortType.DEFAULT -> { backToDefaultCoinList() }
                }
            }

            PriceType.ACC_TRADE_24 -> {
                when(sortType) {
                    SortType.UP -> coinList.sortBy { it.acc_trade_price_24h }
                    SortType.DOWN -> coinList.sortByDescending { it.acc_trade_price_24h }
                    SortType.DEFAULT -> { backToDefaultCoinList() }
                }
            }

        }
        notifyDataSetChanged()
    }

    private fun backToDefaultCoinList() {
        coinList.sortWith(Comparator { ticker1, ticker2 ->
            // codeList의 각 비트코인 종목 데이터의 code 값이 코드 리스트에서 어떤 인덱스에 해댱하는지 확인
            val index1 = codeList.indexOf(ticker1.code)
            val index2 = codeList.indexOf(ticker2.code)

            when {
                index1 == -1 && index2 == -1 -> 0  // 없는 경우 인덱스는 -1
                index1 == -1 -> 1
                index2 == -1 -> -1
                else -> index1.compareTo(index2)  // codeList에 있는 경우 인덱스 순서대로 정렬
            }
        })

    }


    // 뷰홀더 클래스
    class CoinViewHolder private constructor(val binding : CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ticker) {

            binding.tickerName.text = CoinType.fromKrwAbbr(item.code)
            binding.tickerCode.text = item.code
            binding.currentPrice.text = "%,.2f".format(item.trade_price)
            binding.accTradePrice.text = "%,.0f백만".format((item.acc_trade_price_24h / 1_000_000))

            if(item.change == ChangeType.RISE.toString()) {

                binding.currentPrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.RISE_RED))
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.currentPriceLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.red_box)
                }, 2000)

            } else if (item.change == ChangeType.FALL.toString()) {

                binding.currentPrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.FALL_BLUE))
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.currentPriceLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.blue_box)
                }, 2000)
            }
        }

        companion object {
            fun from(parent: ViewGroup) : CoinViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CoinItemBinding.inflate(layoutInflater, parent, false)

                return CoinViewHolder(binding)
            }
        }
    }
}