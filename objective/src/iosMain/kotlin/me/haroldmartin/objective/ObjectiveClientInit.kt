package me.haroldmartin.objective

object ObjectiveClientInit {
    fun withKey(apiKey: String): ObjectiveClient {
        return ObjectiveClient(apiKey)
    }
}
