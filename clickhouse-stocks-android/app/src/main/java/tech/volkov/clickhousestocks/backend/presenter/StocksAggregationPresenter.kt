package tech.volkov.clickhousestocks.backend.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import tech.volkov.clickhousestocks.backend.dagger.DaggerCompaniesRepositoryComponent
import tech.volkov.clickhousestocks.backend.dagger.DaggerStocksRepositoryComponent
import tech.volkov.clickhousestocks.backend.model.AggregateFunction
import tech.volkov.clickhousestocks.backend.model.CompanyDto
import tech.volkov.clickhousestocks.backend.model.StocksAggregatedSearchDto
import tech.volkov.clickhousestocks.backend.repository.CompaniesRepository
import tech.volkov.clickhousestocks.backend.repository.StocksRepository
import tech.volkov.clickhousestocks.frontend.view.StocksAggregationView
import javax.inject.Inject

@InjectViewState
class StocksAggregationPresenter : MvpPresenter<StocksAggregationView>() {

    @Inject
    lateinit var companiesRepository: CompaniesRepository

    @Inject
    lateinit var stocksRepository: StocksRepository

    init {
        DaggerCompaniesRepositoryComponent.create().inject(this)
        DaggerStocksRepositoryComponent.create().inject(this)
    }

    fun getCompanies() = uiScope.launch {
        when (val companies = companiesRepository.getCompanies()) {
            null -> viewState.showErrorMessage(COMPANIES_ERROR_MESSAGE)
            else -> viewState.fillInCompanies(companies.map { it.name })
        }
    }

    fun getStocksAggregation(
        dateStart: String,
        dateEnd: String,
        company: String,
        aggregateFunction: AggregateFunction
    ) = uiScope.launch {
        val stocksAggregatedSearchDto = StocksAggregatedSearchDto(
            dateStart, dateEnd, listOf(CompanyDto(company)), aggregateFunction
        )
        when (val aggregatedStocks =
            stocksRepository.getStocksAggregation(stocksAggregatedSearchDto)) {
            null -> viewState.showErrorMessage(STOCKS_ERROR_MESSAGE)
            else -> viewState.fillInStocksAggregation(aggregatedStocks)
        }
    }

    companion object {
        private val uiScope = CoroutineScope(Dispatchers.Main)
        private const val COMPANIES_ERROR_MESSAGE = "Failed to get list of companies"
        private const val STOCKS_ERROR_MESSAGE = "Failed to get aggregated stocks"
    }
}
