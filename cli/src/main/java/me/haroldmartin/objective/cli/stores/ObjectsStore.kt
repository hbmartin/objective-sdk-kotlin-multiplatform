package me.haroldmartin.objective.cli.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import me.haroldmartin.objective.ObjectiveClient
import me.haroldmartin.objective.cli.ObjectsListScreenUiState

class ObjectsStore(
    private val coroutineScope: CoroutineScope,
    objectiveKey: String,
) {
    private val client = ObjectiveClient(objectiveKey)
    private val stateFlow = MutableStateFlow(ObjectsListScreenUiState())

    val state = stateFlow.asStateFlow()

    val selectedIdAndContent: Pair<String, JsonObject?>?
        get() =
            stateFlow.value.selectedRow?.let { selectedRow ->
                stateFlow.value.items?.get(selectedRow)?.let {
                    it.id to it.objectData
                }
            }

    // TODO: pagination
    fun load() =
        coroutineScope.launch {
            stateFlow.value = ObjectsListScreenUiState(items = null)
            val objects = client.getObjects(includeObject = true, limit = 20)
            stateFlow.value =
                ObjectsListScreenUiState(
                    objects
                        .map {
                            ObjectsListScreenUiState.ObjectItem(
                                id = it.id,
                                updatedAt = it.updatedAt ?: "?",
                                objectData = it.objectData,
                            )
                        },
                )
        }

    fun selectUp() {
        val selectedRow = stateFlow.value.selectedRow
        if (selectedRow == null || selectedRow == 0) {
            stateFlow.value =
                stateFlow.value.copy(
                    selectedRow = stateFlow.value.count?.let { it - 1 },
                )
        } else {
            stateFlow.value =
                stateFlow.value.copy(selectedRow = selectedRow - 1)
        }
    }

    fun selectDown() {
        val selectedRow = stateFlow.value.selectedRow
        if (selectedRow == null || selectedRow == stateFlow.value.count?.let { it - 1 }) {
            stateFlow.value =
                stateFlow.value.copy(
                    selectedRow = 0,
                )
        } else {
            stateFlow.value = stateFlow.value.copy(selectedRow = selectedRow + 1)
        }
    }

    fun delete() {
        val objectId =
            stateFlow.value.selectedRow?.let {
                stateFlow.value.items
                    ?.get(it)
                    ?.id
            } ?: return

        updateItemUpdatedAt(objectId, "[deleting...]")
        coroutineScope.launch {
            val didRemove = client.deleteObject(objectId)
            if (didRemove) {
                removeItem(objectId)
            } else {
                updateItemUpdatedAt(objectId, "[error]")
            }
        }
    }

    private fun updateItemUpdatedAt(
        objectId: String,
        message: String,
    ) {
        val updatedItems =
            stateFlow.value.items?.map {
                if (it.id == objectId) {
                    it.copy(
                        updatedAt = message,
                    )
                } else {
                    it
                }
            }
        stateFlow.value =
            stateFlow.value.copy(items = updatedItems)
    }

    private fun removeItem(objectId: String) {
        val updatedItems =
            stateFlow.value.items?.mapNotNull {
                if (it.id == objectId) {
                    null
                } else {
                    it
                }
            }
        stateFlow.value =
            stateFlow.value.copy(items = updatedItems)
    }
}
