package tech.volkov.clickhousestockscore.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.TimeZone

@Configuration
class ObjectMapperConfiguration {

    @Bean
    fun clickHouseObjectMapper(): ObjectMapper = jacksonObjectMapper()
        .findAndRegisterModules()
        .registerModule(JavaTimeModule())
        .setTimeZone(TimeZone.getDefault())
}
