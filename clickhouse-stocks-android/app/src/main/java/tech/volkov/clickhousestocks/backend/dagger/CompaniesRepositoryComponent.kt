package tech.volkov.clickhousestocks.backend.dagger

import dagger.Component
import tech.volkov.clickhousestocks.backend.presenter.StocksAggregationPresenter
import tech.volkov.clickhousestocks.backend.presenter.StocksChartsPresenter

@Component
interface CompaniesRepositoryComponent {

    fun inject(stocksChartsPresenter: StocksChartsPresenter)

    fun inject(stocksAggregationPresenter: StocksAggregationPresenter)
}
