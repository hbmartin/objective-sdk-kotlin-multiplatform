package me.haroldmartin.objective

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.IO
import me.haroldmartin.objective.models.Id
import me.haroldmartin.objective.models.Index
import me.haroldmartin.objective.models.IndexConfiguration
import me.haroldmartin.objective.models.IndexId
import me.haroldmartin.objective.models.IndexStatusResponse
import me.haroldmartin.objective.models.IndexStatuses
import me.haroldmartin.objective.models.Indexes
import me.haroldmartin.objective.models.ObjectContainer
import me.haroldmartin.objective.models.ObjectId
import me.haroldmartin.objective.models.ObjectStatusContainer
import me.haroldmartin.objective.models.ObjectsResponse
import kotlin.coroutines.CoroutineContext

private const val API_BASE_URL = "https://api.objective.inc/v1/"

class ObjectiveClient(
    apiKey: String,
    ioDispatcher: CoroutineContext = kotlinx.coroutines.Dispatchers.IO,
) {
    val httpClient = ApiClient(API_BASE_URL, apiKey, ioDispatcher)

    suspend fun getIndexes(): List<Index> =
        httpClient.get("indexes").body<Indexes>().indexes

    // TODO: URL encode indexId
    suspend fun getIndexStatus(indexId: IndexId): IndexStatuses =
        httpClient.get("indexes/$indexId/status").body<IndexStatusResponse>().status

    suspend fun createIndex(indexConfiguration: IndexConfiguration): IndexId =
        httpClient.post("indexes", indexConfiguration).body<Id>().id

    // TODO: URL encode indexId
    suspend fun deleteIndex(indexId: IndexId): Boolean =
        httpClient.delete("indexes/$indexId").status.isSuccess()

    // TODO: search

    // TODO: URL encode objectId
    suspend inline fun <reified T> getObject(objectId: ObjectId): ObjectStatusContainer<T> =
        httpClient
            .get("objects/$objectId")
            .bodyOrThrow<ObjectStatusContainer<T>>()

    suspend inline fun <reified T : Any?> getObjects(
        includeObject: Boolean = false,
        includeMetadata: Boolean = false,
        limit: Int = 10,
        cursor: String? = null,
    ): List<ObjectContainer<T>> =
        httpClient
            .get(
                "objects?include_object=$includeObject&include_metadata=$includeMetadata&limit=$limit" +
                    if (cursor != null) "&cursor=$cursor" else "",
            ).body<ObjectsResponse<T>>()
            .objects

    suspend fun createObject(jsonObject: Any): ObjectId =
        httpClient.post("objects", jsonObject).body<Id>().id

    // TODO: URL encode objectId
    suspend fun <T : Any> upsertObject(
        objectId: ObjectId,
        jsonObject: T,
    ): ObjectId = httpClient.put("objects/$objectId", jsonObject).body<Id>().id

    suspend fun deleteObject(objectId: ObjectId): Boolean =
        httpClient.delete("objects/$objectId").let {
            return it.status.isSuccess()
        }
}

suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T = if (this.status.isSuccess()) {
    this.body<T>()
} else {
    throw ObjectiveApiError(this.status)
}
