package tech.volkov.clickhousestockscore.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.Date

private const val CLICKHOUSE_DATE_PATTERN = "yyyy-MM-dd"
private val CLICKHOUSE_DATE_FORMAT = SimpleDateFormat(CLICKHOUSE_DATE_PATTERN)

data class StockEntity(
    val company: String,
    @JsonFormat(pattern = CLICKHOUSE_DATE_PATTERN)
    val date: Date,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Float
)

fun ResultSet.toStockEntity() = StockEntity(
    company = getString("company"),
    date = CLICKHOUSE_DATE_FORMAT.parse(getString("date")),
    open = getFloat("open"),
    high = getFloat("high"),
    low = getFloat("low"),
    close = getFloat("close"),
    volume = getFloat("volume")
)
