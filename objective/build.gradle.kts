import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.dokka)
    id("co.touchlab.kmmbridge") version "0.5.7"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3"
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "objective"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
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
    }
}

apiValidation {
    ignoredPackages.addAll(listOf("kotlinx.coroutines.internal", "kotlinx.serialization.internal.GeneratedSerializer"))
    ignoredClasses.addAll(listOf("me.haroldmartin.objective.ApiClient", "me.haroldmartin.objective.HttpEngineKt"))
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("me.haroldmartin", "objective-sdk", "0.3.0")

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

kmmbridge {
    version = "0.3.0"
    mavenPublishArtifacts()
    spm(spmDirectory = rootDir.path)
}
