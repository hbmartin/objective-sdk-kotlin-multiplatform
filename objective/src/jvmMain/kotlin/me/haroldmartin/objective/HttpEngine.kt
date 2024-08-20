package me.haroldmartin.objective

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val HTTP_ENGINE: HttpClientEngine = OkHttp.create()

actual fun defaultDispatcher(): CoroutineContext = Dispatchers.IO
