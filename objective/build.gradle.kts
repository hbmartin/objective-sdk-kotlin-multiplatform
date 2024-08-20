import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.dokka)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()

    val xcf = XCFramework()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "objective"
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "objective.js"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "objective"
            xcf.add(this)
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation("net.thauvin.erik.urlencoder:urlencoder-lib:1.5.0")

            api(libs.ktor.serialization.kotlinx.json)
            api(libs.kotlinx.datetime)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(npm("@js-joda/core", "3.2.0"))
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("me.haroldmartin", "objective-sdk", "0.1.1")

    pom {
        name.set("Objective SDK")
        description.set("Client SDK for Objective Inc. object, indexing, and search.")
        inceptionYear.set("2024")
        url.set("https://github.com/hbmartin/objective-sdk-kotlin-multiplatform")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("hmartin")
                name.set("Harold Martin")
                url.set("https://github.com/hbmartin/")
            }
        }
        scm {
            url.set("https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/")
            connection.set("scm:git:git://github.com/hbmartin/objective-sdk-kotlin-multiplatform.git")
            developerConnection.set("scm:git:ssh://git@github.com/hbmartin/objective-sdk-kotlin-multiplatform.git")
        }
    }
}
