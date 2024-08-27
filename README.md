# Objective Kotlin Multiplatform SDK

[![Detekt](https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/actions/workflows/lint-and-test.yml/badge.svg)](https://github.com/hbmartin/objective-sdk-kotlin-multiplatform/actions/workflows/lint-and-test.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/hbmartin/objective-sdk-kotlin-multiplatform/badge)](https://www.codefactor.io/repository/github/hbmartin/objective-sdk-kotlin-multiplatform)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=hbmartin_objective-sdk-kotlin-multiplatform&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=hbmartin_objective-sdk-kotlin-multiplatform)
[![Documentation](https://img.shields.io/badge/Documentation-3d3d41?logo=kotlin)](https://hbmartin.github.io/objective-sdk-kotlin-multiplatform/index.html)
[![Maven Central Version](https://img.shields.io/maven-central/v/me.haroldmartin/objective-sdk)](https://central.sonatype.com/artifact/me.haroldmartin/objective-sdk)

## Install and run the CLI / TUI

Clone this repository to run the CLI / TUI or install with Homebrew.

```shell
# Set your API key
export OBJECTIVE_KEY=sk_...
brew install hbmartin/objective/objective
```

## Install and use the SDK library

### Add a dependency to your `build.gradle.kts` file:

(To add the dependency to non-Gradle projects see the snippets on [Maven Central](https://central.sonatype.com/artifact/me.haroldmartin/objective-sdk).)

```kotlin
implementation("me.haroldmartin:objective-sdk:0.1.5")
```

If Gradle cannot find the Objective artifact then check that `mavenCentral()` is in your `repositories`.

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

// Note that the generic can be any @Serializable object
// https://ktor.io/docs/client-serialization.html#receive_send_data
// It is strongly suggested you use nullable types to avoid deserialization errors
val obj = client.getObject<JsonObject>("objectId")
```

## Java / Spring Usage

ObjectiveClient methods are `suspend`ing (async) and so require the explicit use of [continuations](https://www.baeldung.com/kotlin/suspend-functions-from-java) in Java.

For an example see this [Spring Boot demo](https://github.com/hbmartin/springboot-demo/blob/main/src/main/java/com/example/demo/DemoApplication.java#L28).

## API Documentation

All of the client calls available are [documented here](https://hbmartin.github.io/objective-sdk-kotlin-multiplatform/objective/me.haroldmartin.objective/-objective-client/index.html) 
and the model used in responses are documented for [index calls](https://hbmartin.github.io/objective-sdk-kotlin-multiplatform/objective/me.haroldmartin.objective.models.index/index.html) 
and [object calls](https://hbmartin.github.io/objective-sdk-kotlin-multiplatform/objective/me.haroldmartin.objective.models.obj/index.html).

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