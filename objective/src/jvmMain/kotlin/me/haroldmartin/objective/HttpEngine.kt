package me.haroldmartin.objective

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual val HTTP_ENGINE: HttpClientEngine = OkHttp.create()
