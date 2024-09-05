package me.haroldmartin.objective

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.IO
import me.haroldmartin.objective.models.index.Id
import me.haroldmartin.objective.models.index.Index
import me.haroldmartin.objective.models.index.IndexConfiguration
import me.haroldmartin.objective.models.index.IndexId
import me.haroldmartin.objective.models.index.IndexStatusResponse
import me.haroldmartin.objective.models.index.IndexStatuses
import me.haroldmartin.objective.models.index.Indexes
import me.haroldmartin.objective.models.obj.ObjectContainer
import me.haroldmartin.objective.models.obj.ObjectId
import me.haroldmartin.objective.models.obj.ObjectStatusContainer
import me.haroldmartin.objective.models.obj.ObjectsResponse
import me.haroldmartin.objective.models.obj.SearchResultsResponse
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmOverloads

private const val API_BASE_URL = "https://api.objective.inc/v1/"

@Suppress("TooManyFunctions", "StringLiteralDuplication", "CascadingCallWrapping")
class ObjectiveClient @JvmOverloads constructor(
    apiKey: String,
    val autoUrlEncodeIds: Boolean = true,
    ioDispatcher: CoroutineContext = kotlinx.coroutines.Dispatchers.IO,
) {
    val httpClient = ApiClient(API_BASE_URL, apiKey, ioDispatcher)

    // Index calls

    suspend fun getIndexes(): List<Index> = httpClient.get("indexes").body<Indexes>().indexes

    suspend fun getIndexStatus(indexId: IndexId): IndexStatuses =
        httpClient.get("indexes/${indexId.encodeUrlOrThrow()}/status").body<IndexStatusResponse>().status

    suspend fun createIndex(indexConfiguration: IndexConfiguration): IndexId =
        httpClient.post("indexes", indexConfiguration).body<Id>().id

    suspend fun deleteIndex(indexId: IndexId): Boolean =
        httpClient.delete("indexes/${indexId.encodeUrlOrThrow()}").status.isSuccess()

    @Suppress("LongParameterList")
    suspend inline fun <reified T : Any?> search(
        indexId: IndexId,
        query: String,
        limit: Int = 10,
        offset: Int = 0,
        filterQuery: String? = null,
        objectFields: String? = null,
    ): SearchResultsResponse<T> =
        httpClient
            .get(
                "indexes/${indexId.encodeUrlOrThrow()}/search?query=${UrlEncoderUtil.encode(query)}" +
                    "&limit=$limit&offset=$offset" +
                    if (filterQuery != null) {
                        "&filter_query=$filterQuery"
                    } else {
                        "" +
                            if (objectFields != null) "&object_fields=$objectFields" else ""
                    },
            ).body<SearchResultsResponse<T>>()

    // Object calls

    suspend inline fun <reified T> getObject(objectId: ObjectId): ObjectStatusContainer<T> =
        httpClient
            .get("objects/${objectId.encodeUrlOrThrow()}")
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

    suspend inline fun <reified T : Any> createObject(serializableObject: T): ObjectId =
        httpClient.post("objects", serializableObject).body<Id>().id

    suspend inline fun <reified T : Any> upsertObject(
        objectId: ObjectId,
        serializableObject: T,
    ): ObjectId = httpClient.put<T>("objects/${objectId.encodeUrlOrThrow()}", serializableObject).body<Id>().id

    suspend fun deleteObject(objectId: ObjectId): Boolean =
        httpClient.delete("objects/${objectId.encodeUrlOrThrow()}").let {
            return it.status.isSuccess()
        }

    inline fun String.encodeUrlOrThrow(): String {
        val encoded = UrlEncoderUtil.encode(this)
        return if (encoded == this) {
            this
        } else {
            if (autoUrlEncodeIds) {
                encoded
            } else {
                throw UnencodedIdException(this)
            }
        }
    }
}

/** @suppress */
suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T =
    if (this.status.isSuccess()) {
        this.body<T>()
    } else {
        throw ObjectiveApiException(this.status)
    }
