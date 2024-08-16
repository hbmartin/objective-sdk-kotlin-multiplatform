package me.haroldmartin.objective.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ObjectId = String

@Serializable
data class ObjectContainer<T : Any?>(
    val id: ObjectId,
    @SerialName("date_created")
    val createdAt: String?, // TODO: datetime
    @SerialName("date_updated")
    val updatedAt: String?, // TODO: datetime
    @SerialName("object")
    val objectData: T?,
)

@Serializable
data class IndexIdStatus(
    @SerialName("id")
    val indexId: String,
    val status: String,
)

@Serializable
data class IndexesStatus(
    val indexes: List<IndexIdStatus>,
) : Map<String, IndexStatus> by indexes.associate(transform = {
        it.indexId to IndexStatus.valueOf(it.status)
    })

@Serializable
data class ObjectStatusContainer<T>(
    val id: ObjectId,
    val status: IndexesStatus,
    @SerialName("object")
    val objectData: T,
)

@Serializable
data class ObjectsResponse<T : Any?>(
    val objects: List<ObjectContainer<T>>,
    val pagination: Pagination,
    val metadata: Metadata? = null,
)
