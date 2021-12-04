package tech.volkov.clickhousestocksreader.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.volkov.clickhousestocksreader.ClickHouseStocksReaderApplication

@Configuration
class OpenApiConfiguration {

    @Bean
    fun groupedOpenApi(): GroupedOpenApi = GroupedOpenApi.builder()
            .group("public")
            .packagesToScan(ClickHouseStocksReaderApplication::class.java.`package`.name)
            .build()

    @Bean
    fun openApi(): OpenAPI = OpenAPI().info(Info().title("ClickHouse Reader API"))
}
