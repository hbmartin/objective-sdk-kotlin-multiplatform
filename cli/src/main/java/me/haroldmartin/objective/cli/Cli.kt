package me.haroldmartin.objective.cli

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.haroldmartin.objective.ObjectiveClient
import java.io.File

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

fun searchIndex(
    objectiveKey: String,
    indexId: String,
    query: String,
    objectFields: String? = null,
) = runBlocking {
    val client = ObjectiveClient(objectiveKey)
    client.search<JsonObject>(indexId, query, objectFields = objectFields).results.forEach {
        println(it.id)
        println(it.objectData)
    }
}

@Suppress("TooGenericExceptionCaught")
fun createObject(
    objectiveKey: String,
    fileName: String,
) = runBlocking {
    val fileContents: String
    try {
        fileContents = File(fileName).readText()
    } catch (e: Exception) {
        println("Error reading $fileName : ${e.message}")
        return@runBlocking
    }
    val jsonData: JsonObject
    try {
        jsonData = Json.decodeFromString<JsonObject>(fileContents)
    } catch (e: Exception) {
        println("Error deserializing $fileName : ${e.message}")
        return@runBlocking
    }
    val id = ObjectiveClient(objectiveKey).createObject(jsonData)
    println("$id <= $fileName")
}
