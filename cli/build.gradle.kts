import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("application")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.mavenPublish)
}

application {
    mainClass.set("me.haroldmartin.objective.cli.MainKt")
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "me.haroldmartin.objective.cli.MainKt"
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath
            .get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}

dependencies {
    implementation(projects.objective)
    implementation("com.jakewharton.mosaic:mosaic-runtime:0.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    implementation("org.jline:jline:3.26.3")
    implementation("org.slf4j:slf4j-nop:2.0.15")
    detektPlugins("io.nlopez.compose.rules:detekt:0.4.10")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("me.haroldmartin", "objective-cli", "0.1.4")

    pom {
        name.set("Objective CLI")
        description.set("CLI / TUI for Objective Inc. object, indexing, and search.")
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
