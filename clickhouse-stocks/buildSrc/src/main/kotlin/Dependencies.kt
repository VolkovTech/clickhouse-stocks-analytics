import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

object Jvm {
    const val version = "1.8"
}

object Kotlin {
    const val version = "1.5.31"

    const val stdlibJdk8 = "kotlin-stdlib-jdk8"
    const val reflect = "kotlin-reflect"
    const val jvmId = "jvm"
    const val kaptId = "kapt"
    const val allOpenId = "plugin.allopen"
    const val noArgId = "plugin.noarg"

    const val logging = "io.github.microutils:kotlin-logging:2.0.11"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
}

object Detect {
    const val version = "1.18.1"
    const val id = "io.gitlab.arturbosch.detekt"
}

object Spring {
    const val group = "org.springframework"
    const val contextSupport = "$group:spring-context-support"
    const val dependencyManagement = "io.spring.dependency-management"

    object Cloud {
        const val version = "2020.0.4"
        const val group = "${Spring.group}.cloud"
        const val dependencies = "$group:spring-cloud-dependencies:$version"
    }

    object Boot {
        const val version = "2.5.6"
        const val group = "${Spring.group}.boot"
        const val starterDataJpa = "$group:spring-boot-starter-data-jpa"
        const val starterWebServices = "$group:spring-boot-starter-web-services"
        const val starterActuator = "$group:spring-boot-starter-actuator"
        const val configurationProcessor = "$group:spring-boot-configuration-processor"
        const val webflux = "$group:spring-boot-starter-webflux"
    }
}

object Common {
    object Libraries {

        object Metrics {
            const val micrometer = "io.micrometer:micrometer-registry-prometheus"
        }

        object OpenApi {
            private const val version = "1.5.12"
            const val dependency = "org.springdoc:springdoc-openapi-ui:$version"
        }

        object Test {
            private const val junitJupiterVersion = "5.7.1"
            private const val junitPlatformVersion = "1.7.1"

            val implementation = listOf(
                "io.mockk:mockk:1.10.0",
                "org.assertj:assertj-core:2.6.0",
                "org.mockito:mockito-core:2.24.0",
                "org.mockito:mockito-junit-jupiter:2.23.0",
                "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0",
                "com.nhaarman:mockito-kotlin:1.6.0",
                "org.springframework.boot:spring-boot-starter-test",
                "io.kotest:kotest-runner-junit5-jvm:4.3.2",
                "org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion"
            )
            val runtime = listOf(
                "org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion",
                "org.junit.vintage:junit-vintage-engine:$junitJupiterVersion",
                "org.junit.platform:junit-platform-launcher:$junitPlatformVersion",
                "org.junit.platform:junit-platform-runner:$junitPlatformVersion",
                "org.junit.platform:junit-platform-engine:$junitPlatformVersion",
                "org.junit.platform:junit-platform-commons:$junitPlatformVersion"
            )
        }
    }

    const val jacksonModuleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0"
    const val apacheCommonLang = "org.apache.commons:commons-lang3:3.7"
    const val logback = "net.logstash.logback:logstash-logback-encoder:5.2"
}

object ClickHouse {
    const val clickHouseJdbc = "ru.yandex.clickhouse:clickhouse-jdbc:0.3.1"
    const val clickHouseTestContainer = "org.testcontainers:clickhouse:1.16.2"
}

fun PluginDependenciesSpec.kotlinPlugin(plugin: String): PluginDependencySpec = kotlin(plugin).version(Kotlin.version)
fun PluginDependenciesSpec.kotlinPlugins(vararg plugins: String) = plugins.forEach { kotlinPlugin(it) }

fun PluginAware.applyPlugin(plugin: String) = apply(plugin = plugin)
fun PluginAware.applyPlugins(vararg plugins: String) = plugins.forEach { applyPlugin(it) }
