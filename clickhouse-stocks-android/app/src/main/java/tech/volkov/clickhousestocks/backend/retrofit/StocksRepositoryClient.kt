package tech.volkov.clickhousestocks.backend.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import tech.volkov.clickhousestocks.backend.model.AggregatedResultDto
import tech.volkov.clickhousestocks.backend.model.StockDto
import tech.volkov.clickhousestocks.backend.model.StocksAggregatedSearchDto
import tech.volkov.clickhousestocks.backend.model.StocksSearchDto

interface StocksRepositoryClient {

    @GET("/api/v1/stocks")
    fun getStocks(@Body stocksSearchDto: StocksSearchDto): Call<Map<String, List<StockDto>>>

    @GET("/api/v1//stocks/aggregation")
    fun getAggregatedStocks(
        @Body stocksAggregatedSearchDto: StocksAggregatedSearchDto
    ): Call<AggregatedResultDto>
}
