package tech.volkov.clickhousestocksreader.service

import tech.volkov.clickhousestocksreader.dto.AggregatedResultDto
import tech.volkov.clickhousestocksreader.dto.StockDto
import tech.volkov.clickhousestocksreader.dto.StocksAggregatedSearchDto
import tech.volkov.clickhousestocksreader.dto.StocksSearchDto

interface ClickHouseStocksService {

    fun getStocks(stocksSearchDto: StocksSearchDto): Map<String, List<StockDto>>

    fun getStocksAggregation(stocksAggregatedSearchDto: StocksAggregatedSearchDto): Map<String, AggregatedResultDto>
}
