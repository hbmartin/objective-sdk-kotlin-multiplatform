package me.haroldmartin.objective.cli.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.haroldmartin.objective.ObjectiveClient
import me.haroldmartin.objective.cli.IndexesListScreenUiState

class IndexesStore(
    private val coroutineScope: CoroutineScope,
    objectiveKey: String,
) {
    private val client = ObjectiveClient(objectiveKey)
    private val indexesScreenUiStateFlow = MutableStateFlow(IndexesListScreenUiState())
    val state = indexesScreenUiStateFlow.asStateFlow()

    fun load() =
        coroutineScope.launch {
            val indexes = client.getIndexes()
            indexesScreenUiStateFlow.value =
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
                val updatedItems =
                    indexesScreenUiStateFlow.value.items?.map {
                        if (it.id == index.id) {
                            it.copy(
                                uploaded = status.uploaded,
                                processing = status.processing,
                                ready = status.ready,
                                error = status.error,
                            )
                        } else {
                            it
                        }
                    }
                indexesScreenUiStateFlow.value = indexesScreenUiStateFlow.value.copy(items = updatedItems)
            }
        }

    fun refresh() {
        val indexId =
            indexesScreenUiStateFlow.value.selectedRow?.let {
                indexesScreenUiStateFlow.value.items
                    ?.get(it)
                    ?.id
            } ?: return
        val updatedItems =
            indexesScreenUiStateFlow.value.items?.map {
                if (it.id == indexId) {
                    it.copy(
                        uploaded = null,
                        processing = null,
                        ready = null,
                        error = null,
                    )
                } else {
                    it
                }
            }
        indexesScreenUiStateFlow.value =
            indexesScreenUiStateFlow.value.copy(items = updatedItems)
        coroutineScope.launch {
            val status = client.getIndexStatus(indexId)
            val updatedItems =
                indexesScreenUiStateFlow.value.items?.map {
                    if (it.id == indexId) {
                        it.copy(
                            uploaded = status.uploaded,
                            processing = status.processing,
                            ready = status.ready,
                            error = status.error,
                        )
                    } else {
                        it
                    }
                }
            indexesScreenUiStateFlow.value =
                indexesScreenUiStateFlow.value.copy(items = updatedItems)
        }
    }

    fun selectUp() {
        val selectedRow = indexesScreenUiStateFlow.value.selectedRow
        if (selectedRow == null || selectedRow == 0) {
            indexesScreenUiStateFlow.value =
                indexesScreenUiStateFlow.value.copy(
                    selectedRow = indexesScreenUiStateFlow.value.count?.let { it - 1 },
                )
        } else {
            indexesScreenUiStateFlow.value =
                indexesScreenUiStateFlow.value.copy(selectedRow = selectedRow - 1)
        }
    }

    fun selectDown() {
        val selectedRow = indexesScreenUiStateFlow.value.selectedRow
        if (selectedRow == null || selectedRow == indexesScreenUiStateFlow.value.count?.let { it - 1 }) {
            indexesScreenUiStateFlow.value =
                indexesScreenUiStateFlow.value.copy(
                    selectedRow = 0,
                )
        } else {
            indexesScreenUiStateFlow.value = indexesScreenUiStateFlow.value.copy(selectedRow = selectedRow + 1)
        }
    }
}
