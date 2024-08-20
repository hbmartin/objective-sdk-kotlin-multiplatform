package me.haroldmartin.objective.cli

import com.jakewharton.mosaic.runMosaicBlocking
import kotlinx.coroutines.awaitCancellation
import me.haroldmartin.objective.cli.palletes.parseArgsForColorsPalette

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

private fun runUi(args: Array<String>) =
    when {
        args.size == 2 || (args.size == 4 && args.indexOf("-c") != -1) -> runMosaicBlocking {
            val colorsPalette = parseArgsForColorsPalette(args)
            val viewModel = ViewModel(this, parseArgsForObjectiveKey(args))

            setContent {
                App(viewModel, colorsPalette)
            }

            awaitCancellation()
        }
        else -> runCliCommand(args)
    }
