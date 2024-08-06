import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    id("com.vanniktech.maven.publish") version "0.29.0"
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()

    val xcf = XCFramework()
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
    }
}

mavenPublishing {
    coordinates("com.example.mylibrary", "mylibrary-runtime", "1.0.3-SNAPSHOT")

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
            developerConnection.set("scm:git:ssh://git@github.com/uhbmartin/objective-sdk-kotlin-multiplatform.git")
        }
    }
}
