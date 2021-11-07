package tech.volkov.clickhousestocks.frontend.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import tech.volkov.clickhousestocks.backend.model.AggregatedResultDto

@StateStrategyType(AddToEndSingleStrategy::class)
interface StocksAggregationView : MvpView {

    fun fillInCompanies(companies: List<String>)

    fun fillInStocksAggregation(aggregatedResultDto: AggregatedResultDto)

    fun showErrorMessage(message: String)
}
