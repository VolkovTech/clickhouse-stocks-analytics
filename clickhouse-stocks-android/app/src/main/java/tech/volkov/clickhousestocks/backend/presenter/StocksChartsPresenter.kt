package tech.volkov.clickhousestocks.backend.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import tech.volkov.clickhousestocks.backend.dagger.DaggerCompaniesRepositoryComponent
import tech.volkov.clickhousestocks.backend.dagger.DaggerStocksRepositoryComponent
import tech.volkov.clickhousestocks.backend.model.CompanyDto
import tech.volkov.clickhousestocks.backend.model.StocksSearchDto
import tech.volkov.clickhousestocks.backend.repository.CompaniesRepository
import tech.volkov.clickhousestocks.backend.repository.StocksRepository
import tech.volkov.clickhousestocks.frontend.view.StocksChartsView
import javax.inject.Inject

@InjectViewState
class StocksChartsPresenter : MvpPresenter<StocksChartsView>() {

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

    fun getStocks(
        dateStart: String,
        dateEnd: String,
        company: String
    ) = uiScope.launch {
        val stocksSearchDto = StocksSearchDto(dateStart, dateEnd, listOf(CompanyDto(company)))
        when(val stocks = stocksRepository.getStocks(stocksSearchDto)) {
            null -> viewState.showErrorMessage(STOCKS_ERROR_MESSAGE)
            else -> viewState.fillInCharts(stocks[company]!!)
        }
    }

    companion object {
        private val uiScope = CoroutineScope(Dispatchers.Main)
        private const val COMPANIES_ERROR_MESSAGE = "Failed to get list of companies"
        private const val STOCKS_ERROR_MESSAGE = "Failed to get stocks"
    }
}
