# Objective Kotlin Multiplatform SDK

[![PR Checks](https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/actions/workflows/pre-merge.yml/badge.svg)](https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/actions/workflows/pre-merge.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/hbmartin/objective-sdk-kotlin-multiplatform/badge)](https://www.codefactor.io/repository/github/hbmartin/objective-sdk-kotlin-multiplatform)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=hbmartin_objective-sdk-kotlin-multiplatform&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=hbmartin_intellij-build-webhook-notifier)
[![Documentation](https://img.shields.io/badge/Documentation-3d3d41?logo=kotlin)](https://hbmartin.github.io/objective-sdk-kotlin-multiplatform/index.html)
[![Maven Central Version](https://img.shields.io/maven-central/v/me.haroldmartin/objective-sdk-kotlin-multiplatform)](https://central.sonatype.com/artifact/me.haroldmartin/objective-sdk-kotlin-multiplatform)

## Quick Start

### Add a dependency to your `build.gradle.kts` file:

To add the dependency to non-Gradle projects see the snippets on [Maven Central](https://search.maven.org/artifact/me.haroldmartin/objective-sdk-kotlin-multiplatform).

```kotlin
implementation("me.haroldmartin:objective-sdk:0.0.1")
```

This is published to maven central so if gradle cannot find the artifact check that `mavenCentral()` is in your `repositories`.

### Instantiate the SDK

```kotlin
import me.haroldmartin.objective.ObjectiveClient


// To get your API key, sign up at https://objective.ai and paste it below
val objectiveKey = "sk_qwerty"

// To retrieve your key from an environment variable, use:
// val objectiveKey = System.getenv("OBJECTIVE_KEY")

val client = ObjectiveClient(objectiveKey)
```

### Get an object

```kotlin
import kotlinx.serialization.json.JsonObject

// Note that the generic can be any Ktor serializable object
// https://ktor.io/docs/client-serialization.html#receive_send_data
// It is strongly suggested you use nullable types to avoid deserialization errors
val obj = client.getObject<JsonObject>("objectId")
```

## Contributing

* Jump in and modify this project! Start by cloning it with `git clone git@github.com:hbmartin/objective-sdk-kotlin-multiplatform.git`, then open it in IntelliJ and run the tests.
* [PRs](https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/pulls) and [bug reports / feature requests](https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/issues) are all welcome!
* This project is linted with [ktlint](https://github.com/pinterest/ktlint) and statically checked with [detekt](https://github.com/detekt/detekt)
* Treat other people with helpfulness, gratitude, and consideration! See the [JetBrains CoC](https://confluence.jetbrains.com/display/ALL/JetBrains+Open+Source+and+Community+Code+of+Conduct)

## Authors

* [Harold Martin](https://www.linkedin.com/in/harold-martin-98526971/) - harold.martin at gmail

## Legal

* This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE.txt) file for details
* “Objective” and any associated logos are the trademarks of Objective AI, Inc. (“Objective Trademarks”). All right, title, and interest, including all intellectual property rights, in the Platform, APIs, Service, AI Models, and Resultant Data (collectively, “Objective IP”) are owned by Objective AI, Inc.