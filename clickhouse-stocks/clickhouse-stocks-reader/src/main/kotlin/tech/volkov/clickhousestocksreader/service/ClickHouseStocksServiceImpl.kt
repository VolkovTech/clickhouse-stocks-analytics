package tech.volkov.clickhousestocksreader.service

import org.springframework.stereotype.Service
import tech.volkov.clickhousestockscore.configuration.StocksClickHouseProperties
import tech.volkov.clickhousestockscore.entity.CLICKHOUSE_DATE_FORMAT
import tech.volkov.clickhousestockscore.entity.StockEntity
import tech.volkov.clickhousestockscore.entity.toStockEntity
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestocksreader.dto.AggregatedResultDto
import tech.volkov.clickhousestocksreader.dto.StockDto
import tech.volkov.clickhousestocksreader.dto.StocksAggregatedSearchDto
import tech.volkov.clickhousestocksreader.dto.StocksSearchDto
import tech.volkov.clickhousestocksreader.dto.AggregateFunction

@Service
class ClickHouseStocksServiceImpl(
    private val clickHouseStorage: ClickHouseStorage,
    private val stocksClickHouseProperties: StocksClickHouseProperties
) : ClickHouseStocksService {

    override fun getStocks(stocksSearchDto: StocksSearchDto): Map<String, List<StockDto>> {
        val stockEntities = mutableListOf<StockEntity>()

         clickHouseStorage.selectByQuery(stocksSearchDto.toQuery()) {
             while (it.next()) {
                 stockEntities.add(it.toStockEntity())
             }
             stockEntities
        }

        val resultMap = mutableMapOf<String, MutableList<StockDto>>()

        stockEntities.forEach {
            if (resultMap[it.company] != null) {
                resultMap[it.company]!!.add(it.toStockDto())
            } else {
                resultMap[it.company] = mutableListOf(it.toStockDto())
            }
        }

        return resultMap
    }

    private fun StocksSearchDto.toQuery() = buildString {
        append("select * from ${stocksClickHouseProperties.table} ")
        append("where ")
        append("date >= '$dateStart' and date <= '$dateEnd' and ")
        append("company in (${companies.joinToString { "'$it'" }})")
    }

    private fun StockEntity.toStockDto() = StockDto(
        date = CLICKHOUSE_DATE_FORMAT.format(date),
        open = open,
        high = high,
        low = low,
        close = close,
        volume = volume
    )

    override fun getStocksAggregation(
        stocksAggregatedSearchDto: StocksAggregatedSearchDto
    ): Map<String, AggregatedResultDto> {
        val stockEntities = mutableListOf<StockEntity>()

        clickHouseStorage.selectByQuery(stocksAggregatedSearchDto.toQuery()) {
            while (it.next()) {
                stockEntities.add(it.toStockEntity(isDateExcluded = true))
            }
            stockEntities
        }

        val resultMap = mutableMapOf<String, AggregatedResultDto>()

        stockEntities.forEach {
            resultMap[it.company] = AggregatedResultDto(
                function = stocksAggregatedSearchDto.function,
                open = it.open,
                high = it.high,
                low = it.low,
                close = it.close,
                volume = it.volume
            )
        }

        return resultMap
    }

    private fun StocksAggregatedSearchDto.toQuery() = buildString {
        append("select company, ${function.toSqlColumns()} from ${stocksClickHouseProperties.table} ")
        append("where ")
        append("date >= '$dateStart' and date <= '$dateEnd' and ")
        append("company in (${companies.joinToString { "'$it'" }}) ")
        append("group by company")
    }

    private fun AggregateFunction.toSqlColumns() = when (this) {
        AggregateFunction.COUNT -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "count")
        AggregateFunction.MIN -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "min")
        AggregateFunction.MAX -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "max")
        AggregateFunction.SUM -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "sum")
        AggregateFunction.AVG -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "avg")
        AggregateFunction.QUANTILE_50 -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "quantile(0.5)")
        AggregateFunction.QUANTILE_75 -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "quantile(0.75)")
        AggregateFunction.QUANTILE_90 -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "quantile(0.90)")
        AggregateFunction.QUANTILE_95 -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "quantile(0.95)")
        AggregateFunction.QUANTILE_99 -> AGGREGATE_COLUMNS_TEMPLATE.replace("function", "quantile(0.99)")
    }

    companion object {
        private const val AGGREGATE_COLUMNS_TEMPLATE =
                "function(open) as open, function(high) as high, function(low) as low, " +
                        "function(close) as close, function(volume) as volume"
    }
}
