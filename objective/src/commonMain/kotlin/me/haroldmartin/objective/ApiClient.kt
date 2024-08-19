package me.haroldmartin.objective

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

/** @suppress */
@OptIn(ExperimentalSerializationApi::class)
class ApiClient(
    host: String,
    apiKey: String,
    val ioDispatcher: CoroutineContext,
) {
    val httpClient =
        HttpClient(engine = HTTP_ENGINE) {
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        explicitNulls = false
                        ignoreUnknownKeys = true
                        useAlternativeNames = false
                    },
                )
            }

            defaultRequest {
                url {
                    takeFrom(host)
                }
                headers {
                    append("Authorization", "Bearer $apiKey")
                    append("User-Agent", "objective-kotlin/0.1.0")
                }
            }
        }

    suspend inline fun <reified T : Any> post(
        path: String,
        requestBody: T,
    ): HttpResponse =
        withContext(ioDispatcher) {
            httpClient
                .post(path) {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }.body()
        }

    suspend inline fun <reified T : Any> put(
        path: String,
        requestBody: T,
    ): HttpResponse =
        withContext(ioDispatcher) {
            httpClient.put(path) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
        }

    suspend inline fun get(path: String): HttpResponse =
        withContext(ioDispatcher) {
            httpClient.get(path)
        }

    suspend inline fun delete(path: String): HttpResponse =
        withContext(ioDispatcher) {
            httpClient.delete(path)
        }
}
