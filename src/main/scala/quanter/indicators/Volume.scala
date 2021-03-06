/**
  *
  */
package quanter.indicators

import org.quant4s.data.market.TradeBar
import quanter.indicators.window.{ReadOnlyWindow, WindowIndicator}

/**
  * 放量，缩量指标
  * 1: 表示放量   0：没有表示   -1：表示缩量
  */
class Volume(pname: String, pperiod: Int, epsilon: Double = 0.2) extends WindowIndicator[TradeBar](pname, pperiod) {
  def this (period: Int) {
    this("VOL_" + period, period, 0.2)
  }

  override protected def computeNextValue(window: ReadOnlyWindow[TradeBar], input: TradeBar): Double = {
    if(isReady) {
//      val vol: Long = window.reduce((a,b) => a.volume + b.volume)
      var vol: Long = 0
      val sum:(TradeBar => Double) = (a => {
        vol += a.volume
        vol
      })
      window.foreach(sum)
      if(((vol / window.size) * (1+epsilon)).toLong < input.volume) 1
      else if(((vol / window.size) * (1-epsilon)).toLong > input.volume) -1
      else 0
    } else
      0
  }

}
