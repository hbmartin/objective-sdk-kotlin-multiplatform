package me.haroldmartin.objective.cli

import com.jakewharton.mosaic.runMosaicBlocking
import kotlinx.coroutines.awaitCancellation
import me.haroldmartin.objective.cli.palletes.parseArgsForColorsPalette
import kotlin.system.exitProcess

private const val HELP_MESSAGE = """
    Usage: objective-cli [-k OBJECTIVE_KEY] [COMMAND]
    
    Commands:
        sk_<COMMAND>    Run a specific command
        -h, --help      Show this help message
"""

fun main(args: Array<String>) =
    when {
        args.isEmpty() -> {
            println("EMPTY ARGS")
            System.getenv("OBJECTIVE_KEY")?.let {
                runUi(arrayOf("-k", it))
            } ?: println(HELP_MESSAGE)
        }

        args[0] == "-h" || args[0] == "---help" -> println(HELP_MESSAGE)
        args[0].startsWith("sk_") -> runUi(arrayOf("-k") + args)
        args[0] == "-k" -> runUi(args)
        else -> {
            println("NON EMPTY ARGS")
            System.getenv("OBJECTIVE_KEY")?.let {
                println((arrayOf("-k", it) + args).joinToString())
                runUi(arrayOf("-k", it) + args)
            } ?: println("Make sure you have a key\n" + HELP_MESSAGE)
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
            listIndexes(objectiveKey)
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
