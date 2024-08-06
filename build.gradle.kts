plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    id("com.github.ben-manes.versions") version "0.51.0"
    alias(libs.plugins.detekt)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
        detektPlugins(rootProject.libs.detekt.ruleauthors)
    }
    detekt {
        reports {
            xml.required.set(true)
            txt.required.set(false)
            html.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = JavaVersion.VERSION_11.toString()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    checkForGradleUpdate = true
    rejectVersionIf {
        listOf("-beta-", "-alpha", "-beta02", "2.0.10-", "2.0.20-").any { word ->
            candidate.version.contains(word)
        }
    }
}
