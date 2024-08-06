package me.haroldmartin.objective.cli

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jakewharton.mosaic.LocalTerminal
import com.jakewharton.mosaic.layout.background
import com.jakewharton.mosaic.layout.fillMaxWidth
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.text.SpanStyle
import com.jakewharton.mosaic.text.buildAnnotatedString
import com.jakewharton.mosaic.text.withStyle
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.haroldmartin.objective.cli.common.ConfirmationDialog
import me.haroldmartin.objective.cli.common.Key
import me.haroldmartin.objective.cli.common.key
import me.haroldmartin.objective.cli.common.keyEventFlow
import me.haroldmartin.objective.cli.common.utf16CodePoint
import me.haroldmartin.objective.cli.palletes.ColorsPalette
import me.haroldmartin.objective.cli.palletes.LocalColorsPalette
import me.haroldmartin.objective.cli.screens.IndexesScreen
import me.haroldmartin.objective.cli.screens.ObjectsScreen

@Composable
fun App(
    viewModel: ViewModel,
    colorsPalette: ColorsPalette,
) {
    val terminal = LocalTerminal.current
    val uiState by viewModel.uiStateFlow.collectAsState()
    CompositionLocalProvider(LocalColorsPalette provides colorsPalette) {
        Box(
            modifier =
                Modifier
                    .width(terminal.size.width)
                    .height(terminal.size.height - 4) // subtraction of one is necessary, because there is a line with a cursor at the bottom, which moves up all the content
                    .background(LocalColorsPalette.current.mainBg),
        ) {
            when (uiState.screenUiState) {
                is IndexesListScreenUiState ->
                    IndexesScreen(
                        uiState.screenUiState as IndexesListScreenUiState,
                        modifier = Modifier.padding(bottom = 1),
                    )
                is ObjectsListScreenUiState ->
                    ObjectsScreen(
                        uiState.screenUiState as ObjectsListScreenUiState,
                        modifier = Modifier.padding(bottom = 1),
                    )

                is DialogScreenUiState ->
                    ConfirmationDialog(
                        title = (uiState.screenUiState as DialogScreenUiState).title,
                        message = (uiState.screenUiState as DialogScreenUiState).message,
                        titleColor = LocalColorsPalette.current.callsTitleFg,
                        borderColor = LocalColorsPalette.current.dangerBg,
                        modifier = Modifier.padding(horizontal = 1),
                    )
            }
            BottomStatusBar(
                currentScreen = UiState.Screen.fromUiState(uiState.screenUiState),
                commonInfo = (uiState.screenUiState as? ListScreenUiState)?.commonInfo ?: "",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(1)
                        .padding(horizontal = 1)
                        .align(Alignment.BottomCenter),
            )
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            keyEventFlow.collect { keyEvent ->
                when (keyEvent.key) {
                    Key.DirectionLeft -> viewModel.onArrowLeftPress()
                    Key.DirectionRight -> viewModel.onArrowRightPress()
                    Key.DirectionUp -> viewModel.onArrowUpPress()
                    Key.DirectionDown -> viewModel.onArrowDownPress()
                    Key.Tab -> viewModel.onTabPress()
                    Key.Type -> {
                        if (keyEvent.utf16CodePoint == 'q'.code || keyEvent.utf16CodePoint == 'Q'.code) {
                            viewModel.onQPress()
                        } else if (keyEvent.utf16CodePoint == 'i'.code || keyEvent.utf16CodePoint == 'I'.code) {
                            viewModel.onIPress()
                        } else if (keyEvent.utf16CodePoint == 'o'.code || keyEvent.utf16CodePoint == 'O'.code) {
                            viewModel.onOPress()
                        } else if (keyEvent.utf16CodePoint == 'r'.code || keyEvent.utf16CodePoint == 'R'.code) {
                            viewModel.onRPress()
                        } else if (keyEvent.utf16CodePoint == 'd'.code || keyEvent.utf16CodePoint == 'D'.code) {
                            viewModel.onDPress()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomStatusBar(
    currentScreen: UiState.Screen,
    commonInfo: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(LocalColorsPalette.current.menuBg)) {
        Text(
            buildAnnotatedString {
                UiState.Screen.entries.forEachIndexed { index, screen ->
                    if (screen == currentScreen) {
                        withStyle(SpanStyle(LocalColorsPalette.current.menuHighlightFg)) {
                            append(screen.displayName)
                        }
                    } else {
                        append(screen.displayName)
                    }
                    if (index < UiState.Screen.entries.lastIndex) {
                        withStyle(SpanStyle(LocalColorsPalette.current.menuDividerFg)) {
                            append(" | ")
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterStart),
            color = LocalColorsPalette.current.menuFg,
        )
        Text(
            commonInfo,
            color = LocalColorsPalette.current.statusBarFg,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}
