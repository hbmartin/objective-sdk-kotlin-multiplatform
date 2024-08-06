package me.haroldmartin.objective.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

typealias ObjectId = String

@Serializable
data class ObjectContainer(
    val id: ObjectId,
    @SerialName("date_created")
    val createdAt: String?, // TODO: datetime
    @SerialName("date_updated")
    val updatedAt: String?, // TODO: datetime
    @SerialName("object")
    var objectData: JsonObject?,
)

@Serializable
data class ObjectsResponse(
    val objects: List<ObjectContainer>,
    val pagination: Pagination,
    val metadata: Metadata? = null,
)
