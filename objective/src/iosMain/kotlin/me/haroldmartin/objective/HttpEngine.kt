package me.haroldmartin.objective

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.IO
import kotlin.coroutines.CoroutineContext

actual val HTTP_ENGINE: HttpClientEngine = Darwin.create()

actual fun defaultDispatcher(): CoroutineContext = kotlinx.coroutines.Dispatchers.IO
