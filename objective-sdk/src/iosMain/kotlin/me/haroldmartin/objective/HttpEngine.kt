package me.haroldmartin.objective

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual val HTTP_ENGINE: HttpClientEngine = Darwin.create()
