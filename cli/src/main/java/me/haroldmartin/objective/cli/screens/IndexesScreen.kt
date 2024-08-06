package me.haroldmartin.objective.cli.screens

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Text
import me.haroldmartin.objective.cli.IndexesListScreenUiState
import me.haroldmartin.objective.cli.common.BorderedTitledBox
import me.haroldmartin.objective.cli.common.Table
import me.haroldmartin.objective.cli.common.TableConfig
import me.haroldmartin.objective.cli.common.TableData
import me.haroldmartin.objective.cli.palletes.LocalColorsPalette

@Composable
fun IndexesScreen(
    uiState: IndexesListScreenUiState,
    modifier: Modifier = Modifier,
) {
    BorderedTitledBox(
        title = "Indexes",
        titleColor = LocalColorsPalette.current.callsTitleFg,
        borderColor = LocalColorsPalette.current.callsBorderFg,
        modifier = modifier,
    ) {
        if (uiState.items != null) {
            Table(
                tableData = TableData(uiState.items),
                selectedRow = uiState.selectedRow,
                tableConfig =
                    TableConfig(
                        titleColor = LocalColorsPalette.current.callsTableHeaderFg,
                        columnConfigs =
                            listOf(
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "id",
                                    stringFromItem = { it.id },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "updated at",
                                    stringFromItem = { it.updatedAt },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "uploaded",
                                    stringFromItem = { it.uploaded?.toString() ?: "..." },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "ready",
                                    stringFromItem = { it.ready?.toString() ?: "..." },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "processing",
                                    stringFromItem = { it.processing?.toString() ?: "..." },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "error",
                                    stringFromItem = { it.error?.toString() ?: "..." },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                ),
                            ),
                    ),
            )
        } else {
            Text("Loading data, please wait")
        }
    }
}
