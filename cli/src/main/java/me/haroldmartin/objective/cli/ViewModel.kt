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
import me.haroldmartin.objective.cli.stores.IndexesStore
import me.haroldmartin.objective.cli.stores.ObjectsStore
import kotlin.system.exitProcess

class ViewModel(
    private val coroutineScope: CoroutineScope,
    objectiveApiKey: String,
) {
    private val currentScreenFlow = MutableStateFlow(UiState.Screen.Indexes)
    private val indexesStore = IndexesStore(coroutineScope, objectiveApiKey)
    private val objectsStore = ObjectsStore(coroutineScope, objectiveApiKey)
    private val dialogScreenFlow = MutableStateFlow(DialogScreenUiState("", ""))

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
        if (currentScreenFlow.value == UiState.Screen.Indexes) {
            indexesStore.selectUp()
        } else if (currentScreenFlow.value == UiState.Screen.Objects) {
            objectsStore.selectUp()
        }
    }

    fun onArrowDownPress() {
        if (currentScreenFlow.value == UiState.Screen.Indexes) {
            indexesStore.selectDown()
        } else if (currentScreenFlow.value == UiState.Screen.Objects) {
            objectsStore.selectDown()
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
        if (currentScreenFlow.value == UiState.Screen.Indexes) {
            indexesStore.refresh()
        } else if (currentScreenFlow.value == UiState.Screen.Objects) {
            objectsStore.load()
        }
    }

    fun onDPress() {
        dialogScreenFlow.value = DialogScreenUiState("Delete Thingy", "Are you super sure?")
        currentScreenFlow.value = UiState.Screen.Dialog
        if (currentScreenFlow.value == UiState.Screen.Indexes) {
            // Show confirmation dialog
        } else if (currentScreenFlow.value == UiState.Screen.Objects) {
//            objectsStore.delete()
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
