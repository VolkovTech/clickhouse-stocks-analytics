package tech.volkov.clickhousestocks.backend.model

/**
 * /stocks
 */
// request
data class StocksSearchDto(
    val dateStart: String,
    val dateEnd: String,
    val companies: List<CompanyDto>,
    val columns: List<Column> = listOf(
        Column.OPEN, Column.HIGH, Column.LOW, Column.CLOSE, Column.VOLUME
    )
)

enum class Column {
    OPEN, HIGH, LOW, CLOSE, VOLUME
}

// response
data class StockDto(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

/**
 * /stocks/aggregation
 */
// request
data class StocksAggregatedSearchDto(
    val dateStart: String,
    val dateEnd: String,
    val companies: List<CompanyDto>,
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

fun String.toAggregateFunction(): AggregateFunction? = when (this) {
    AggregateFunction.COUNT.name -> AggregateFunction.COUNT
    AggregateFunction.MIN.name -> AggregateFunction.MIN
    AggregateFunction.MAX.name -> AggregateFunction.MAX
    AggregateFunction.SUM.name -> AggregateFunction.SUM
    AggregateFunction.AVG.name -> AggregateFunction.AVG
    AggregateFunction.QUANTILE_50.name -> AggregateFunction.QUANTILE_50
    AggregateFunction.QUANTILE_75.name -> AggregateFunction.QUANTILE_75
    AggregateFunction.QUANTILE_90.name -> AggregateFunction.QUANTILE_90
    AggregateFunction.QUANTILE_95.name -> AggregateFunction.QUANTILE_95
    AggregateFunction.QUANTILE_99.name -> AggregateFunction.QUANTILE_99
    else -> null
}

// response
data class AggregatedResultDto(
    val function: AggregateFunction,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)
