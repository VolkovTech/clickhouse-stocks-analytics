plugins {
    id(Spring.Boot.group) version Spring.Boot.version

    kotlinPlugins(Kotlin.jvmId, Kotlin.kaptId)
    kotlinPlugin(Kotlin.allOpenId) apply false // provides kotlin-spring
    kotlinPlugin(Kotlin.noArgId) apply false // provides kotlin-jpa

    id(Detect.id) version Detect.version apply false
}

val scriptsDir: File by extra { rootProject.projectDir.toPath().resolve("gradle-scripts").toFile() }

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}

allprojects {
    group = "tech.volkov"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    applyPlugins("kotlin", "kotlin-kapt", "kotlin-spring")

    applyPlugin("java")
    applyPlugin(Spring.Boot.group)
    applyPlugin(Spring.dependencyManagement)
    apply(from = "$scriptsDir/detekt.gradle")

    tasks {
        withType<Jar> {
            destinationDirectory.set(file("${project.buildDir}"))
        }
        jar {
            archiveBaseName.set(project.name)
            archiveVersion.set(project.version.toString())
        }
        // needed for correct generation of ide hints for application.yml
        compileJava {
            dependsOn(processResources)
        }
        compileKotlin {
            kotlinOptions.jvmTarget = Jvm.version
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = Jvm.version
        }
        test {
            useJUnitPlatform()
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom(Spring.Cloud.dependencies)
        }
    }

    dependencies {
        // kotlin
        kotlin(Kotlin.stdlibJdk8)
        kotlin(Kotlin.reflect)
        implementation(Kotlin.logging)
        implementation(Kotlin.coroutines)
        implementation(Spring.contextSupport)
        implementation(Spring.Boot.starterWebServices)
        implementation(Spring.Boot.starterActuator)

        runtimeOnly(Common.Libraries.Metrics.micrometer)

        implementation(Common.jacksonModuleKotlin)

        // annotation processing
        implementation(group = "com.squareup", name = "kotlinpoet", version = "1.7.2")
        implementation(group = "com.google.auto.service", name = "auto-service", version = "1.0-rc7")
        kapt(group = "com.google.auto.service", name = "auto-service", version = "1.0-rc7")

        implementation(Common.Libraries.OpenApi.dependency)

        // utils
        compileOnly(Spring.Boot.configurationProcessor)
        implementation(Common.apacheCommonLang)

        // logback
        implementation(Common.logback)

        // test
        Common.Libraries.Test.implementation.forEach { testImplementation(it) }
        Common.Libraries.Test.runtime.forEach { testRuntimeOnly(it) }
    }
}
