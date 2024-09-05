package me.haroldmartin.objective.cli

internal const val HELP_MESSAGE = """
    Usage: objective [-k OBJECTIVE_KEY] [COMMAND]
    
    Commands:
        If no command is given, display the TUI
        [o]bjects       List objects or pass an object ID to get a single object
        [i]ndexes       List indexes or pass an index ID to get a single index
        [s]earch        Pass an Index ID and a search term to query an index
        co              Create an object from JSON file[s]
"""

internal fun parseArgsForObjectiveKey(args: Array<String>): String {
    val keyIndex = args.indexOf("-k")
    if (keyIndex < 0 || keyIndex >= args.lastIndex) {
        throw NoObjectiveApiKeyError()
    }
    return args[keyIndex + 1]
}

class NoObjectiveApiKeyError : Error(
    "No Objective API key found in OBJECTIVE_KEY env var or -k flag",
)
