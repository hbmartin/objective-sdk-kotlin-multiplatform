package me.haroldmartin.objective.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Index(
    val id: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
)

@Serializable
data class Indexes(
    val indexes: List<Index>,
    val pagination: Pagination,
)

typealias IndexId = String

@Serializable
data class Id(
    val id: IndexId,
)

@Serializable
data class IndexConfiguration(
    @SerialName("index_type")
    val indexType: IndexType = IndexType(),
    val fields: Fields,
)

@Serializable
enum class IndexTypeName {
    @SerialName("multimodal")
    MULTIMODAL,

    @SerialName("text")
    TEXT,

    @SerialName("text-rainbow")
    TEXT_RAINBOW,

    @SerialName("multimodal-hybrid")
    MULTIMODAL_HYBRID,

    @SerialName("text-hybrid")
    TEXT_HYBRID,

    @SerialName("turbo-only")
    TURBO_ONLY,

    @SerialName("turbo-only-with-6-shards")
    TURBO_ONLY_WITH_6_SHARDS,

    @SerialName("turbo-only-with-12-shards")
    TURBO_ONLY_WITH_12_SHARDS,

    @SerialName("turbo-only-with-24-shards")
    TURBO_ONLY_WITH_24_SHARDS,

    @SerialName("image")
    IMAGE,
}

@Serializable
data class IndexType(
    val name: IndexTypeName = IndexTypeName.TEXT,
    val highlights: Highlights? = null,
    val finetuning: Finetuning? = null,
    val version: String? = null,
)

@Serializable
data class Highlights(
    val text: Boolean,
)

@Serializable
data class Finetuning(
    @SerialName("base_index_id")
    val baseIndexId: String,
    val feedback: List<Feedback>,
)

@Serializable
data class Feedback(
    val query: String,
    @SerialName("object_id")
    val objectId: String,
    val label: FinetuneLabel,
)

@Serializable
enum class FinetuneLabel {
    GREAT,
    OK,
    BAD,
}

@Serializable
data class Fields(
    val searchable: FieldConfig,
    val crawlable: FieldConfig? = null,
    val filterable: FieldConfig? = null,
    val types: Map<String, String>? = null,
    @SerialName("segment_delimiter")
    val segmentDelimiter: Map<String, String>? = null,
)

@Serializable
data class FieldConfig(
    val allow: List<String>,
    val deny: List<String>,
)

@Serializable
data class IndexStatusResponse(
    val status: IndexStatuses,
)

@Serializable
enum class IndexStatus {
    UPLOADED,
    PROCESSING,
    READY,
    ERROR,
}

@Serializable
data class IndexStatuses(
    @SerialName("UPLOADED")
    val uploaded: Int,
    @SerialName("PROCESSING")
    val processing: Int,
    @SerialName("READY")
    val ready: Int,
    @SerialName("ERROR")
    val error: Int,
) : Map<IndexStatus, Int> by mapOf(
    IndexStatus.UPLOADED to uploaded,
    IndexStatus.PROCESSING to processing,
    IndexStatus.READY to ready,
    IndexStatus.ERROR to error,
)
