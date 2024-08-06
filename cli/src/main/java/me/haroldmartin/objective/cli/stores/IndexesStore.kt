package me.haroldmartin.objective.cli.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.haroldmartin.objective.ObjectiveClient
import me.haroldmartin.objective.cli.IndexesListScreenUiState
import me.haroldmartin.objective.models.IndexStatus

class IndexesStore(
    private val coroutineScope: CoroutineScope,
    objectiveKey: String,
) {
    private val client = ObjectiveClient(objectiveKey)
    private val stateFlow = MutableStateFlow(IndexesListScreenUiState())
    val state = stateFlow.asStateFlow()
    val selectedIdAndStatuses: Pair<String, List<String>>?
        get() =
            stateFlow.value.selectedRow?.let { selectedRow ->
                stateFlow.value.items?.get(selectedRow)?.let {
                    it.id to it.objectsStatuses
                }
            }

    fun load() =
        coroutineScope.launch {
            val indexes = client.getIndexes()
            stateFlow.value =
                IndexesListScreenUiState(
                    indexes
                        .map {
                            IndexesListScreenUiState.IndexItem(
                                id = it.id,
                                updatedAt = it.updatedAt,
                            )
                        },
                )

            indexes.forEach { index ->
                val status = client.getIndexStatus(index.id)
                stateFlow.value =
                    stateFlow.value.copy(
                        items = updateStatuses(index.id, status),
                    )
            }
        }

    fun refresh() {
        val indexId = selectedId ?: return
        stateFlow.value =
            stateFlow.value.copy(items = updateStatuses(indexId, null))
        coroutineScope.launch {
            val status = client.getIndexStatus(indexId)
            stateFlow.value =
                stateFlow.value.copy(items = updateStatuses(indexId, status))
        }
    }

    private fun updateStatuses(
        indexId: String,
        status: IndexStatus?,
    ) = stateFlow.value.items?.map {
        if (it.id == indexId) {
            it.copy(
                uploaded = status?.uploaded,
                processing = status?.processing,
                ready = status?.ready,
                error = status?.error,
            )
        } else {
            it
        }
    }

    private val selectedId: String?
        get() =
            stateFlow.value.selectedRow?.let { selectedRow ->
                stateFlow.value.items
                    ?.get(selectedRow)
                    ?.id
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
        val indexId = selectedId ?: return

        updateItemUpdatedAt(indexId, "[deleting...]")
        coroutineScope.launch {
            val didRemove = client.deleteIndex(indexId)
            if (didRemove) {
                removeItem(indexId)
            } else {
                updateItemUpdatedAt(indexId, "[error]")
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
