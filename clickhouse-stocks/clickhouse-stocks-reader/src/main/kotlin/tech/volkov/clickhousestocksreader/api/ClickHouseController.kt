package tech.volkov.clickhousestocksreader.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.volkov.clickhousestocksreader.dto.AggregatedResultDto
import tech.volkov.clickhousestocksreader.dto.StockDto
import tech.volkov.clickhousestocksreader.dto.StocksAggregatedSearchDto
import tech.volkov.clickhousestocksreader.dto.StocksSearchDto
import tech.volkov.clickhousestocksreader.service.ClickHouseStocksService

@RestController
@RequestMapping("/api/v1")
class ClickHouseController(
    private val clickHouseStocksService: ClickHouseStocksService
) {

    @GetMapping("stocks")
    fun getStocks(@RequestBody stocksSearchDto: StocksSearchDto): Map<String, List<StockDto>> {
        return clickHouseStocksService.getStocks(stocksSearchDto)
    }

    @GetMapping("stocks/aggregation")
    fun getStocksAggregation(
        @RequestBody stocksAggregatedSearchDto: StocksAggregatedSearchDto
    ): Map<String, AggregatedResultDto> {
        return clickHouseStocksService.getStocksAggregation(stocksAggregatedSearchDto)
    }
}
