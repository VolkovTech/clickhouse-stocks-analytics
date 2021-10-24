package tech.volkov.clickhousestockscore.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import ru.yandex.clickhouse.ClickHouseDataSource
import ru.yandex.clickhouse.settings.ClickHouseProperties

@Configuration
class ClickHouseConfiguration {

    @Bean
    fun clickHouseDatasource(stocksClickHouseProperties: StocksClickHouseProperties): ClickHouseDataSource {
        val clickHouseProperties = ClickHouseProperties().apply {
            user = stocksClickHouseProperties.username
            password = stocksClickHouseProperties.password
        }
        return ClickHouseDataSource(stocksClickHouseProperties.jdbcUrl, clickHouseProperties)
    }
}

@Component
@ConfigurationProperties("clickhouse")
class StocksClickHouseProperties {
    lateinit var jdbcUrl: String
    lateinit var table: String

    lateinit var username: String
    lateinit var password: String
}
