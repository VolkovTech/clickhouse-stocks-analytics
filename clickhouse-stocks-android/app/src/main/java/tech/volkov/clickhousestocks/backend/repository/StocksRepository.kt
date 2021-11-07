package tech.volkov.clickhousestocks.backend.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.volkov.clickhousestocks.backend.model.*
import tech.volkov.clickhousestocks.backend.retrofit.RetrofitBuilder
import tech.volkov.clickhousestocks.backend.retrofit.StocksRepositoryClient
import tech.volkov.clickhousestocks.backend.utils.getOrNull
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class StocksRepository @Inject constructor() {

    private val client = RetrofitBuilder.retrofit.create(StocksRepositoryClient::class.java)

    suspend fun getStocks(
        stocksSearchDto: StocksSearchDto
    ): Map<String, List<StockDto>>? = withContext(Dispatchers.IO) {
        return@withContext getOrNull { client.getStocks(stocksSearchDto).execute().body() }
    }

    suspend fun getStocksAggregation(
        stocksAggregatedSearchDto: StocksAggregatedSearchDto
    ): AggregatedResultDto? = withContext(Dispatchers.IO) {
        return@withContext getOrNull { client.getAggregatedStocks(stocksAggregatedSearchDto).execute().body() }
    }
}
