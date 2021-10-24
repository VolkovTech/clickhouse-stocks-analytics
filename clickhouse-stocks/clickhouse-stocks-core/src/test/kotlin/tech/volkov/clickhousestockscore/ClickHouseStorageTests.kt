package tech.volkov.clickhousestockscore

import io.kotest.assertions.asClue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.apache.commons.lang3.time.DateUtils
import org.junit.Test
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.ClickHouseContainer
import tech.volkov.clickhousestockscore.configuration.ClickHouseConfiguration
import tech.volkov.clickhousestockscore.configuration.ObjectMapperConfiguration
import tech.volkov.clickhousestockscore.configuration.StocksClickHouseProperties
import tech.volkov.clickhousestockscore.entity.StockEntity
import tech.volkov.clickhousestockscore.entity.toStockEntity
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestockscore.storage.ClickHouseStorageImpl
import java.util.Calendar
import java.util.Date

@Suppress("TYPE_MISMATCH_WARNING")
internal class ClickHouseStorageTests {

    private val clickHouseContainer = ClickHouseContainer("yandex/clickhouse-server:21.8.10")
        .withClasspathResourceMapping(
            "users.xml",
            "/etc/clickhouse-server/users.d/users.xml",
            BindMode.READ_ONLY
        )
        .withInitScript("schema.sql")
        .apply { start() }

    private val stocksClickHouseProperties = StocksClickHouseProperties().apply {
        jdbcUrl = "jdbc:clickhouse://${clickHouseContainer.containerIpAddress}:${clickHouseContainer.firstMappedPort}"
        table = "stocks_analytics.stocks"
        username = "admin"
        password = "admin"
    }

    private val clickHouseDatasource = ClickHouseConfiguration().clickHouseDatasource(stocksClickHouseProperties)

    private val clickHouseStorage: ClickHouseStorage =
        ClickHouseStorageImpl(clickHouseDatasource, stocksClickHouseProperties, objectMapper)

    @Test
    fun `save() correctly persists data & getStockEntities() correctly select data`() {
        getAllEntities() shouldHaveSize 0

        clickHouseStorage.save(listOf(stockEntityFirst, stockEntitySecond))

        getAllEntities().asClue {
            it shouldHaveSize 2

            it[0] shouldBe stockEntityFirst
            it[1] shouldBe stockEntitySecond
        }
    }

    private fun getAllEntities(): List<StockEntity> {
        val stockEntities = mutableListOf<StockEntity>()

        clickHouseStorage.selectByQuery("select * from ${stocksClickHouseProperties.table}") {
            while (it.next()) {
                stockEntities.add(it.toStockEntity())
            }
            stockEntities
        }

        return stockEntities
    }

    companion object {
        private val objectMapper = ObjectMapperConfiguration().clickHouseObjectMapper()
        private val date = DateUtils.truncate(Date(1635674247000), Calendar.DATE)

        private val stockEntityFirst =
            StockEntity("company1", date, 2.0f, 5.0f, 1.0f, 1.0f, 10000f)
        private val stockEntitySecond =
            StockEntity("company2", date, 4.0f, 10.0f, 3.0f, 6.0f, 200000f)
    }
}
