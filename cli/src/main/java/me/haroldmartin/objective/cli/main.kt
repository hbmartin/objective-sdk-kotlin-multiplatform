package me.haroldmartin.objective.cli

import com.jakewharton.mosaic.runMosaicBlocking
import kotlinx.coroutines.awaitCancellation
import me.haroldmartin.objective.cli.palletes.parseArgsForColorsPalette
import java.lang.RuntimeException

fun main(args: Array<String>) =
    runMosaicBlocking {
        val colorsPalette = parseArgsForColorsPalette(args)
        val viewModel = ViewModel(this, getObjectiveApiKey(args))

        setContent {
            App(viewModel, colorsPalette)
        }

        awaitCancellation()
    }

fun getObjectiveApiKey(args: Array<String>): String =
    parseArgsForObjectiveKey(args) ?: System.getenv("OBJECTIVE_KEY") ?: run {
        throw RuntimeException(
            "Objective API key not found. Please provide it via the OBJECTIVE_KEY environment variable or the -k command line argument.",
        )
    }

private fun parseArgsForObjectiveKey(args: Array<String>): String? {
    val keyIndex = args.indexOf("-k")
    if (keyIndex < 0 || keyIndex >= args.lastIndex) {
        return null
    }
    return args[keyIndex + 1]
}
