package me.haroldmartin.objective.cli.screens

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Text
import me.haroldmartin.objective.cli.IndexesListScreenUiState
import me.haroldmartin.objective.cli.IndexesListScreenUiState.IndexItem
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
    val valueColor = LocalColorsPalette.current.callsTableRowTop1Fg
    val selectedValueColor = LocalColorsPalette.current.callsTableRowTop2Fg

    val stringColumnConfig: (String, (IndexItem) -> String) -> TableConfig.ColumnConfig.StringColumnConfig<IndexItem> =
        { title, stringFromItem ->
            TableConfig.ColumnConfig.StringColumnConfig<IndexItem>(
                title = title,
                stringFromItem = stringFromItem,
                valueColor = valueColor,
                selectedValueColor = selectedValueColor,
                valueAlignment = TableConfig.ColumnConfig.ColumnAlignment.START,
            )
        }

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
                tableConfig = TableConfig(
                    titleColor = LocalColorsPalette.current.callsTableHeaderFg,
                    columnConfigs = listOf(
                        stringColumnConfig("id") { it.id },
                        stringColumnConfig("updated at") { it.updatedAt },
                        stringColumnConfig("uploaded") { it.uploaded?.toString() ?: "..." },
                        stringColumnConfig("ready") { it.ready?.toString() ?: "..." },
                        stringColumnConfig("processing") { it.processing?.toString() ?: "..." },
                        stringColumnConfig("error") { it.error?.toString() ?: "..." },
                    ),
                ),
            )
        } else {
            Text("Loading data, please wait")
        }
    }
}
