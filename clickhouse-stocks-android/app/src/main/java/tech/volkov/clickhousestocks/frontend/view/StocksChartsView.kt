package tech.volkov.clickhousestocks.frontend.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import tech.volkov.clickhousestocks.backend.model.StockDto

@StateStrategyType(AddToEndSingleStrategy::class)
interface StocksChartsView : MvpView {

    fun fillInCompanies(companies: List<String>)

    fun fillInCharts(stocks: List<StockDto>)

    fun showErrorMessage(message: String)
}
