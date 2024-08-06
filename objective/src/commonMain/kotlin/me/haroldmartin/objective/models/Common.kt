package me.haroldmartin.objective.models

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class Metadata(
    val count: Int,
)
