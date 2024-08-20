package me.haroldmartin.objective

import io.ktor.client.engine.HttpClientEngine
import kotlin.coroutines.CoroutineContext

/** @suppress */
expect val HTTP_ENGINE: HttpClientEngine

expect fun defaultDispatcher(): CoroutineContext
