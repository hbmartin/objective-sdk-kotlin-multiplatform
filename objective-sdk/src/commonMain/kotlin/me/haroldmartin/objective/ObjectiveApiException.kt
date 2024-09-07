package me.haroldmartin.objective

import io.ktor.http.HttpStatusCode

class ObjectiveApiException(
    val status: HttpStatusCode,
) : Exception("Objective API error: $status")

class UnencodedIdException(
    val id: String,
) : Exception("ID has non-URL characters and autoUrlEncodeIds=false: $id")
