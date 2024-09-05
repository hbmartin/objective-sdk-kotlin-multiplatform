package me.haroldmartin.objective.cli

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.haroldmartin.objective.ObjectiveClient
import java.io.File
import kotlin.system.exitProcess

private const val COMMAND_INDEX = 2
private const val ARG_INDEX = 3

@Suppress("CyclomaticComplexMethod")
fun runCliCommand(args: Array<String>) {
    val objectiveKey = parseArgsForObjectiveKey(args)
    exitOnException()
    when {
        args[COMMAND_INDEX].startsWith("o") -> {
            args.getOrNull(ARG_INDEX)?.let { objectId ->
                getObject(objectiveKey, objectId)
            } ?: listObjects(objectiveKey)
            exitProcess(0)
        }

        args[COMMAND_INDEX].startsWith("i") -> {
            args.getOrNull(ARG_INDEX)?.let { objectId ->
                getIndex(objectiveKey, objectId)
            } ?: listIndexes(objectiveKey)

            exitProcess(0)
        }

        args[COMMAND_INDEX].startsWith("s") -> {
            val indexId = args.getOrNull(ARG_INDEX) ?: run {
                println("Usage: objective search [INDEX_ID] [SEARCH_TERM] [FIELDS]")
                exitProcess(1)
            }
            val query = args.getOrNull(ARG_INDEX + 1) ?: run {
                println("Usage: objective search [INDEX_ID] [SEARCH_TERM] [FIELDS]")
                exitProcess(1)
            }
            searchIndex(
                objectiveKey = objectiveKey,
                indexId = indexId,
                query = query,
                objectFields = args.getOrNull(ARG_INDEX + 2),
            )
            exitProcess(0)
        }

        args[COMMAND_INDEX].startsWith("c") -> {
            val fileNames = args.drop(ARG_INDEX)
            if (fileNames.isEmpty()) {
                println("Usage: objective co [JSON_FILE] [OTHER_JSON_FILE]")
                exitProcess(1)
            }
            for (fileName in fileNames) {
                createObject(objectiveKey, fileName)
            }
            exitProcess(0)
        }

        else -> println("Unknown command: ${args[0]}\n$HELP_MESSAGE")
    }
}

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
            .forEach { obj ->
                println("${obj.id} (${obj.updatedAt}):")
                println(obj.objectData)
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
        client.getIndexes().forEach { index ->
            @Suppress("TooGenericExceptionCaught")
            try {
                val status = client.getIndexStatus(index.id)
                println("${index.id} (${index.updatedAt}) => $status")
            } catch (e: Exception) {
                println("${index.id} (${index.updatedAt}) => ${e.message}")
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
    client.search<JsonObject>(indexId, query, objectFields = objectFields).results.forEach { obj ->
        println(obj.id)
        println(obj.objectData)
    }
}

@Suppress("TooGenericExceptionCaught", "LabeledExpression")
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

private fun exitOnException() {
    Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
        println("Exception: ${throwable.message}")
        exitProcess(1)
    }
}
