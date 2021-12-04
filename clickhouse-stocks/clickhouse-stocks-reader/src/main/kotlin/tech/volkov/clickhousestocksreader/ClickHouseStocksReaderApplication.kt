package tech.volkov.clickhousestocksreader

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import tech.volkov.clickhousestockscore.ClickHouseStocksCoreMarker

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class],
    scanBasePackageClasses = [
        ClickHouseStocksReaderApplication::class,
        ClickHouseStocksCoreMarker::class
    ]
)
class ClickHouseStocksReaderApplication

fun main(args: Array<String>) {
    runApplication<ClickHouseStocksReaderApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
