package me.haroldmartin.objective

import io.ktor.http.HttpStatusCode

class ObjectiveApiError(
    val status: HttpStatusCode,
) : Error() {
    override val message: String = "Objective API error: $status"
}
