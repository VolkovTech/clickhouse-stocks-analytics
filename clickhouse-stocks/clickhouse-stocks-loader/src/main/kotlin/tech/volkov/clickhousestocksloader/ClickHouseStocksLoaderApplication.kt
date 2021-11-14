package tech.volkov.clickhousestocksloader

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import tech.volkov.clickhousestockscore.ClickHouseStocksCoreMarker

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class],
    scanBasePackageClasses = [ClickHouseStocksCoreMarker::class],
    scanBasePackages = ["tech.volkov.clickhousestockscore",
                        "tech.volkov.clickhousestocksloader"]
)
class ClickHouseStocksLoaderApplication

fun main(args: Array<String>) {
    runApplication<ClickHouseStocksLoaderApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
