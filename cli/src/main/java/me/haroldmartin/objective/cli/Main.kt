package me.haroldmartin.objective.cli

import com.jakewharton.mosaic.runMosaicBlocking
import kotlinx.coroutines.awaitCancellation
import me.haroldmartin.objective.cli.palletes.parseArgsForColorsPalette
import kotlin.system.exitProcess

private const val HELP_MESSAGE = """
    Usage: objective [-k OBJECTIVE_KEY] [COMMAND]
    
    Commands:
        If no command is given, display the TUI
        [o]bjects       List objects or pass an object ID to get a single object
        [i]ndexes       List indexes or pass an index ID to get a single index
        [s]earch        Pass an Index ID and a search term to query an index
"""

fun main(args: Array<String>) {
    System.setProperty("slf4j.internal.verbosity", "ERROR")
    when {
        args.isEmpty() -> {
            System.getenv("OBJECTIVE_KEY")?.let {
                runUi(arrayOf("-k", it))
            } ?: println(HELP_MESSAGE)
        }

        args[0] == "-h" || args[0] == "--help" -> println(HELP_MESSAGE)
        args[0].startsWith("sk_") -> runUi(arrayOf("-k") + args)
        args[0] == "-k" -> runUi(args)
        else -> {
            System.getenv("OBJECTIVE_KEY")?.let {
                runUi(arrayOf("-k", it) + args)
            } ?: println("Please pass a key with `-k` or set the OBJECTIVE_KEY env var\n" + HELP_MESSAGE)
        }
    }
}

fun runUi(args: Array<String>) =
    when {
        args.size == 2 || (args.size == 4 && args.indexOf("-c") != -1) -> runMosaicBlocking {
            val colorsPalette = parseArgsForColorsPalette(args)
            val viewModel = ViewModel(this, parseArgsForObjectiveKey(args))

            setContent {
                App(viewModel, colorsPalette)
            }

            awaitCancellation()
        }
        args[2].startsWith("o") -> {
            val objectiveKey = parseArgsForObjectiveKey(args)
            args.getOrNull(3)?.let { objectId ->
                getObject(objectiveKey, objectId)
            } ?: listObjects(objectiveKey)
            exitProcess(0)
        }
        args[2].startsWith("i") -> {
            val objectiveKey = parseArgsForObjectiveKey(args)
            args.getOrNull(3)?.let { objectId ->
                getIndex(objectiveKey, objectId)
            } ?: listIndexes(objectiveKey)

            exitProcess(0)
        }
        args[2].startsWith("s") -> {
            val objectiveKey = parseArgsForObjectiveKey(args)
            val indexId = args.getOrNull(3) ?: run {
                println("Usage: objective search [INDEX_ID] [SEARCH_TERM] [FIELDS]")
                exitProcess(1)
            }
            val query = args.getOrNull(4) ?: run {
                println("Usage: objective search [INDEX_ID] [SEARCH_TERM] [FIELDS]")
                exitProcess(1)
            }
            searchIndex(objectiveKey, indexId, query, args.getOrNull(5))
            exitProcess(0)
        }
        else -> {
            println("Unknown command: ${args[0]}\n$HELP_MESSAGE")
        }
    }

private fun parseArgsForObjectiveKey(args: Array<String>): String {
    val keyIndex = args.indexOf("-k")
    if (keyIndex < 0 || keyIndex >= args.lastIndex) {
        throw NoObjectiveApiKeyError
    }
    return args[keyIndex + 1]
}

object NoObjectiveApiKeyError : Error(
    "No Objective API key found in OBJECTIVE_KEY env var or -k flag",
) {
    @Suppress("UnusedPrivateMember")
    private fun readResolve(): Any = NoObjectiveApiKeyError
}
