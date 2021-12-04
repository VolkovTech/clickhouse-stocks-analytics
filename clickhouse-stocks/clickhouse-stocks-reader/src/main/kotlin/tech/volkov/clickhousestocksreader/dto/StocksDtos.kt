package tech.volkov.clickhousestocksreader.dto

// /stocks

data class StocksSearchDto(
    val dateStart: String,
    val dateEnd: String,
    val companies: List<String>,
)

data class StockDto(
    val date: String,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Float
)

// /stocks/aggregation

data class StocksAggregatedSearchDto(
    val dateStart: String,
    val dateEnd: String,
    val companies: List<String>,
    val function: AggregateFunction
)

enum class AggregateFunction {
    COUNT,
    MIN,
    MAX,
    SUM,
    AVG,
    QUANTILE_50,
    QUANTILE_75,
    QUANTILE_90,
    QUANTILE_95,
    QUANTILE_99
}

data class AggregatedResultDto(
    val function: AggregateFunction,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Float
)
