package me.haroldmartin.objective.cli

import androidx.compose.runtime.Immutable
import kotlinx.serialization.json.JsonObject

@Immutable
data class UiState(
    val screenUiState: ScreenUiState = IndexesListScreenUiState(),
) {
    enum class Screen(
        val displayName: String?,
    ) {
        Indexes("[i]ndexes"),
        Objects("[o]bjects"),
        Dialog(null),
        ;

        // n.b. non navigable entries are those with a null displayName
        // these MUST come last in the list

        companion object {
            val navEntries = entries.filter { it.displayName != null }

            fun fromUiState(uiState: ScreenUiState): Screen =
                when (uiState) {
                    is IndexesListScreenUiState -> Indexes
                    is ObjectsListScreenUiState -> Objects
                    is DialogScreenUiState -> Dialog
                }
        }
    }
}

@Immutable
sealed interface ScreenUiState

@Immutable
data class DialogScreenUiState(
    val title: String,
    val messages: List<String>,
) : ScreenUiState

@Immutable
sealed interface ListScreenUiState : ScreenUiState {
    val commonInfo: String
    val selectedRow: Int?
    val count: Int?
}

@Immutable
data class IndexesListScreenUiState(
    val items: List<IndexItem>? = null,
    override val selectedRow: Int? = null,
    override val count: Int? = items?.size,
    override val commonInfo: String = "[r]efresh, [d]elete, [q]uit",
) : ListScreenUiState {
    @Immutable
    data class IndexItem(
        val id: String,
        val updatedAt: String,
        val uploaded: Int? = null,
        val processing: Int? = null,
        val ready: Int? = null,
        val error: Int? = null,
    ) {
        val objectsStatuses: List<String> =
            listOf(
                "Uploaded: $uploaded",
                "Processing: $processing",
                "Ready: $ready",
                "Error: $error",
            )
    }
}

@Immutable
data class ObjectsListScreenUiState(
    val items: List<ObjectItem>? = null,
    override val selectedRow: Int? = null,
    override val count: Int? = items?.size,
    override val commonInfo: String = "[r]efresh, [d]elete, [q]uit",
) : ListScreenUiState {
    @Immutable
    data class ObjectItem(
        val id: String,
        val updatedAt: String,
        val objectData: JsonObject?,
    )
}
