package me.haroldmartin.objective.cli

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import me.haroldmartin.objective.cli.stores.IndexesStore
import me.haroldmartin.objective.cli.stores.ObjectsStore
import java.io.File
import kotlin.system.exitProcess

class ViewModel(
    private val coroutineScope: CoroutineScope,
    objectiveApiKey: String,
) {
    private val currentScreenFlow = MutableStateFlow(UiState.Screen.Indexes)
    private val indexesStore = IndexesStore(coroutineScope, objectiveApiKey)
    private val objectsStore = ObjectsStore(coroutineScope, objectiveApiKey)
    private val dialogScreenFlow = MutableStateFlow(DialogScreenUiState("", emptyList()))
    private var dialogScreenPreviousState: UiState.Screen = UiState.Screen.Indexes

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiStateFlow: StateFlow<UiState> =
        currentScreenFlow
            .flatMapLatest(::currentScreenToStateFlow)
            .map(::UiState)
            .flowOn(Dispatchers.Default)
            .stateIn(coroutineScope, SharingStarted.Lazily, UiState())

    private fun currentScreenToStateFlow(currentScreen: UiState.Screen): StateFlow<ScreenUiState> =
        when (currentScreen) {
            UiState.Screen.Indexes -> indexesStore.state
            UiState.Screen.Objects -> objectsStore.state
            UiState.Screen.Dialog -> dialogScreenFlow
        }

    init {
        indexesStore.load()
        objectsStore.load()
    }

    fun onArrowLeftPress() {
        switchScreenToLeft()
    }

    fun onArrowRightPress() {
        switchScreenToRight()
    }

    fun onArrowUpPress() {
        when (currentScreenFlow.value) {
            UiState.Screen.Indexes -> indexesStore.selectUp()
            UiState.Screen.Objects -> objectsStore.selectUp()
            UiState.Screen.Dialog -> Unit
        }
    }

    fun onArrowDownPress() {
        when (currentScreenFlow.value) {
            UiState.Screen.Indexes -> indexesStore.selectDown()
            UiState.Screen.Objects -> objectsStore.selectDown()
            UiState.Screen.Dialog -> Unit
        }
    }

    fun onTabPress() {
        switchScreenToRight()
    }

    fun onQPress() {
        coroutineScope.cancel()
        exitProcess(0)
    }

    fun onIPress() {
        currentScreenFlow.value = UiState.Screen.Indexes
    }

    fun onOPress() {
        currentScreenFlow.value = UiState.Screen.Objects
    }

    fun onRPress() {
        when (currentScreenFlow.value) {
            UiState.Screen.Indexes -> indexesStore.refresh()
            UiState.Screen.Objects -> objectsStore.load()
            UiState.Screen.Dialog -> Unit
        }
    }

    fun onDPress() {
        when (currentScreenFlow.value) {
            UiState.Screen.Indexes -> {
                indexesStore.selectedIdAndStatuses?.let { (id, statuses) ->
                    dialogScreenPreviousState = UiState.Screen.Indexes
                    dialogScreenFlow.value = DialogScreenUiState("Proceed to delete index `$id` ?", statuses)
                    currentScreenFlow.value = UiState.Screen.Dialog
                }
            }
            UiState.Screen.Objects -> {
                objectsStore.selectedIdAndContent?.let { (id, content) ->
                    dialogScreenPreviousState = UiState.Screen.Objects
                    dialogScreenFlow.value = DialogScreenUiState("Proceed to delete object `$id` ?", content.toStringList())
                    currentScreenFlow.value = UiState.Screen.Dialog
                }
            }
            UiState.Screen.Dialog -> Unit
        }
    }

    fun onNPress() {
        if (currentScreenFlow.value == UiState.Screen.Dialog) {
            currentScreenFlow.value = dialogScreenPreviousState
        }
    }

    fun onYPress() {
        File("objective-cli.log").appendText(
            "currentScreenFlow.value: ${currentScreenFlow.value}\n" +
                "dialogScreenPreviousState: ${dialogScreenPreviousState}\n",
        )
        if (currentScreenFlow.value == UiState.Screen.Dialog) {
            when (dialogScreenPreviousState) {
                UiState.Screen.Objects -> {
                    objectsStore.delete()
                    currentScreenFlow.value = dialogScreenPreviousState
                }
                UiState.Screen.Indexes -> {
                    indexesStore.delete()
                    currentScreenFlow.value = dialogScreenPreviousState
                }
                UiState.Screen.Dialog -> Unit
            }
        }
    }

    private fun switchScreenToLeft() {
        val currentScreen = currentScreenFlow.value
        currentScreenFlow.value =
            UiState.Screen.entries[
                if (currentScreen.ordinal > 0) {
                    currentScreen.ordinal - 1
                } else {
                    UiState.Screen.entries.lastIndex
                },
            ]
    }

    private fun switchScreenToRight() {
        val currentScreen = currentScreenFlow.value
        currentScreenFlow.value =
            UiState.Screen.entries[
                if (currentScreen.ordinal < UiState.Screen.entries.lastIndex) {
                    currentScreen.ordinal + 1
                } else {
                    0
                },
            ]
    }
}

private fun JsonObject?.toStringList(): List<String> =
    this
        ?.entries
        ?.mapIndexed { index, (k, v) ->
            val quoteValue = (v as? JsonPrimitive)?.isString == true
            val value: String = if (quoteValue) "\"$v\"" else v.toString()
            " \"$k\": $value" + if (index < this.size - 1) "," else ""
        }?.let {
            listOf("{") + it + listOf("}")
        } ?: emptyList()
