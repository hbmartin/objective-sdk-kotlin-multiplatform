package me.haroldmartin.objective.cli.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.haroldmartin.objective.ObjectiveClient
import me.haroldmartin.objective.cli.ObjectsListScreenUiState

class ObjectsStore(
    private val coroutineScope: CoroutineScope,
    objectiveKey: String,
) {
    private val client = ObjectiveClient(objectiveKey)
    private val stateFlow = MutableStateFlow(ObjectsListScreenUiState())
    val state = stateFlow.asStateFlow()

    // TODO: pagination
    fun load() =
        coroutineScope.launch {
            stateFlow.value = ObjectsListScreenUiState(items = null)
            val objects = client.getObjects(includeObject = true, limit = 100)
            stateFlow.value =
                ObjectsListScreenUiState(
                    objects
                        .map {
                            ObjectsListScreenUiState.ObjectItem(
                                id = it.id,
                                updatedAt = it.updatedAt ?: "?",
                                objectAsString = it.objectData.toString(),
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
        val indexId =
            stateFlow.value.selectedRow?.let {
                stateFlow.value.items
                    ?.get(it)
                    ?.id
            } ?: return
    }
}