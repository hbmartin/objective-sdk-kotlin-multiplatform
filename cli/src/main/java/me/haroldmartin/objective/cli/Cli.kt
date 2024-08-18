package me.haroldmartin.objective.cli

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import me.haroldmartin.objective.ObjectiveClient

fun getObject(
    objectiveKey: String,
    objectId: String,
) = runBlocking {
    val obj = ObjectiveClient(objectiveKey).getObject<JsonObject>(objectId)
    println(obj.objectData)
    for ((key, value) in obj.status) {
        println("$key => $value")
    }
}

fun listObjects(objectiveKey: String) =
    runBlocking {
        ObjectiveClient(objectiveKey)
            .getObjects<JsonObject>(includeObject = true, limit = 20)
            .forEach {
                println("${it.id} (${it.updatedAt}):")
                println(it.objectData)
            }
    }

fun getIndex(
    objectiveKey: String,
    indexId: String,
) = runBlocking {
    val client = ObjectiveClient(objectiveKey)
    val status = client.getIndexStatus(indexId)
    status.forEach { (key, value) ->
        println("$key => $value")
    }
}

fun listIndexes(objectiveKey: String) =
    runBlocking {
        val client = ObjectiveClient(objectiveKey)
        client.getIndexes().forEach {
            @Suppress("TooGenericExceptionCaught")
            try {
                val status = client.getIndexStatus(it.id)
                println("${it.id} (${it.updatedAt}) => $status")
            } catch (e: Exception) {
                println("${it.id} (${it.updatedAt}) => ${e.message}")
            }
        }
    }
