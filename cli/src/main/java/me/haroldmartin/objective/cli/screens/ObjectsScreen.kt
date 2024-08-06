package me.haroldmartin.objective.cli.screens

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Text
import me.haroldmartin.objective.cli.ObjectsListScreenUiState
import me.haroldmartin.objective.cli.common.BorderedTitledBox
import me.haroldmartin.objective.cli.common.Table
import me.haroldmartin.objective.cli.common.TableConfig
import me.haroldmartin.objective.cli.common.TableData
import me.haroldmartin.objective.cli.palletes.LocalColorsPalette

@Composable
fun ObjectsScreen(
    uiState: ObjectsListScreenUiState,
    modifier: Modifier = Modifier,
) {
    BorderedTitledBox(
        title = "Objects",
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
                                    trimFromEnd = true,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "updated at",
                                    stringFromItem = { it.updatedAt },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                    trimFromEnd = true,
                                ),
                                TableConfig.ColumnConfig.StringColumnConfig(
                                    title = "data",
                                    stringFromItem = { it.objectData?.toString() ?: "" },
                                    valueColor = LocalColorsPalette.current.callsTableRowTop1Fg,
                                    selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg,
                                    valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
                                    weight = 5,
                                ),
                            ),
                    ),
            )
        } else {
            Text("Loading data, please wait")
        }
    }
}
