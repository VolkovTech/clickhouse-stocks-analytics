package tech.volkov.clickhousestockscore.storage

import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Service
import ru.yandex.clickhouse.ClickHouseDataSource
import ru.yandex.clickhouse.domain.ClickHouseFormat
import tech.volkov.clickhousestockscore.configuration.StocksClickHouseProperties
import tech.volkov.clickhousestockscore.entity.StockEntity
import java.sql.ResultSet

@Service
class ClickHouseStorageImpl(
    private val clickHouseDatasource: ClickHouseDataSource,
    private val stocksClickHouseProperties: StocksClickHouseProperties,
    private val clickHouseObjectMapper: ObjectMapper
) : ClickHouseStorage {

    override fun save(stocksEntities: List<StockEntity>) {
        val insertSql = CLICKHOUSE_INSERT_QUERY.format(stocksClickHouseProperties.table)
        val inputStream = stocksEntities
            .joinToString("\n") { clickHouseObjectMapper.writeValueAsString(it) }
            .byteInputStream()

        clickHousePersistenceMetric.record {
            clickHouseDatasource.connection.use {
                it.createStatement()
                    .write()
                    .sql(insertSql)
                    .data(inputStream, ClickHouseFormat.JSONEachRow)
                    .send()
            }
        }
    }

    override fun <T> selectByQuery(query: String, extractFunction: (ResultSet) -> List<T>): List<T> {
        lateinit var result: List<T>

        clickHouseSelectionMetric.record {
            clickHouseDatasource.connection.use {
                val resultSet = it.createStatement().executeQuery(query)
                result = extractFunction(resultSet)
            }
        }

        return result
    }

    companion object {
        private const val CLICKHOUSE_INSERT_QUERY = "INSERT INTO %s (company, date, open, high, low, close, volume)"

        private val clickHousePersistenceMetric = Timer
            .builder("clickhouse_persistence")
            .register(Metrics.globalRegistry)

        private val clickHouseSelectionMetric = Timer
            .builder("clickhouse_selection")
            .register(Metrics.globalRegistry)
    }
}
