package me.haroldmartin.objective

import io.ktor.client.call.body
import kotlinx.serialization.json.JsonObject
import me.haroldmartin.objective.models.Id
import me.haroldmartin.objective.models.Index
import me.haroldmartin.objective.models.IndexConfiguration
import me.haroldmartin.objective.models.IndexId
import me.haroldmartin.objective.models.IndexStatus
import me.haroldmartin.objective.models.IndexStatusResponse
import me.haroldmartin.objective.models.Indexes
import me.haroldmartin.objective.models.ObjectContainer
import me.haroldmartin.objective.models.ObjectId
import me.haroldmartin.objective.models.ObjectsResponse

private const val API_BASE_URL = "https://api.objective.inc/v1/"

class ObjectiveClient(
    apiKey: String,
) {
    private val httpClient = ApiClient(API_BASE_URL, apiKey)

    suspend fun getIndexes(): List<Index> = httpClient.get("indexes").body<Indexes>().indexes

    suspend fun getIndexStatus(indexId: IndexId): IndexStatus = httpClient.get("indexes/$indexId/status").body<IndexStatusResponse>().status

    suspend fun createIndex(indexConfiguration: IndexConfiguration): IndexId = httpClient.post("indexes", indexConfiguration).body<Id>().id

    suspend fun getObjects(
        includeObject: Boolean = false,
        includeMetadata: Boolean = false,
        limit: Int = 10,
        cursor: String? = null,
    ): List<ObjectContainer> =
        httpClient
            .get(
                "objects?include_object=$includeObject&include_metadata=$includeMetadata&limit=$limit" +
                    if (cursor != null) "&cursor=$cursor" else "",
            ).body<ObjectsResponse>()
            .objects

    suspend fun createObject(jsonObject: JsonObject): ObjectId = httpClient.post("objects", jsonObject).body<Id>().id

    suspend fun deleteObject(objectId: ObjectId): Boolean =
        httpClient.delete("objects/$objectId").let {
            return it.status.value in 200..299
        }
}
