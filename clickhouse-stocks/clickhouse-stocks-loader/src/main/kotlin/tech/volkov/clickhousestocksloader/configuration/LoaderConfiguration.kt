package tech.volkov.clickhousestocksloader.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestocksloader.services.DataBatchService
import tech.volkov.clickhousestocksloader.runners.LoaderRunner
import tech.volkov.clickhousestocksloader.services.GeneratorService

@Configuration
class LoaderConfiguration {

    @Bean
    fun loaderRunner(
        dataBatchService: DataBatchService,
        clickHouseStorage: ClickHouseStorage,
        loaderProperties: LoaderProperties,
        generatorService: GeneratorService,
        generatorProperties: GeneratorProperties
    ): LoaderRunner =
        LoaderRunner(dataBatchService, generatorService, clickHouseStorage, loaderProperties.batchSize,
                     generatorProperties)

    @Bean
    fun dataBatchService(): DataBatchService = DataBatchService()

    @Bean
    fun generatorService(): GeneratorService = GeneratorService()
}

@Component
@ConfigurationProperties("loader")
class LoaderProperties {
    var batchSize: Int = 0
}

@Component
@ConfigurationProperties("generator")
class GeneratorProperties {
    /**
     * flag generation is enabled
     */
    var enabled: Boolean = false
    /**
     * first date of generated data points
     */
    lateinit var dateStart: String
    /**
     * last date of generated data points
     */
    lateinit var dateEnd: String
    /**
     * number of random companies to be generated
     */
    var companiesCount: Int = 0
}
