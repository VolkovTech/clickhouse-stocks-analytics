package tech.volkov.clickhousestocksloader.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestocksloader.processors.DataBatchProcessor
import tech.volkov.clickhousestocksloader.runners.LoaderRunner

@Configuration
class LoaderConfiguration {

    @Bean
    fun loaderRunner(
        dataBatchProcessor: DataBatchProcessor,
        clickHouseStorage: ClickHouseStorage,
        loaderProperties: LoaderProperties
    ): LoaderRunner =
        LoaderRunner(dataBatchProcessor, clickHouseStorage, loaderProperties.batchSize)

    @Bean
    fun dataBatchProcessor(): DataBatchProcessor = DataBatchProcessor()
}

@Component
@ConfigurationProperties("loader")
class LoaderProperties {
    var batchSize: Int = 0
}
