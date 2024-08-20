package me.haroldmartin.objective

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val HTTP_ENGINE: HttpClientEngine = Js.create()

actual fun defaultDispatcher(): CoroutineContext = Dispatchers.Main
