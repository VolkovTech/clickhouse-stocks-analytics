plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":clickhouse-stocks-core"))

    implementation(group = "org.springdoc", name = "springdoc-openapi-ui")
}
