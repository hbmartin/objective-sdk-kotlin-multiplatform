package me.haroldmartin.objective.cli.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.jakewharton.mosaic.layout.drawBehind
import com.jakewharton.mosaic.layout.fillMaxSize
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.text.SpanStyle
import com.jakewharton.mosaic.text.buildAnnotatedString
import com.jakewharton.mosaic.text.withStyle
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Spacer
import kotlin.math.roundToInt

private val TableConfig.ColumnConfig.ColumnAlignment.isStart: Boolean
    get() = this == TableConfig.ColumnConfig.ColumnAlignment.START

@Immutable
data class TableData<T>(
    val items: List<T>,
)

@Immutable
data class TableConfig<T>(
    val titleColor: Color,
    val columnConfigs: List<ColumnConfig<T>>,
) {
    @Immutable
    sealed interface ColumnConfig<T> {
        val weight: Int

        @Immutable
        data class StringColumnConfig<T>(
            val title: String,
            val stringFromItem: (T) -> String,
            val valueColor: Color,
            val selectedValueColor: Color = valueColor,
            val valueAlignment: ColumnAlignment = ColumnAlignment.START,
            override val weight: Int = 1,
            val trimFromEnd: Boolean = false,
        ) : ColumnConfig<T>

        @Immutable
        data class ProgressColumnConfig<T>(
            val filledColor: Color,
            val emptyColor: Color,
            val progressFromItem: (T) -> Float,
            override val weight: Int = 1,
        ) : ColumnConfig<T>

        enum class ColumnAlignment {
            START,
            END,
        }
    }
}

@Suppress("LongMethod")
@Composable
fun <T> Table(
    tableData: TableData<T>,
    tableConfig: TableConfig<T>,
    selectedRow: Int? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .drawBehind {
                val weights = tableConfig.columnConfigs.sumOf { it.weight }
                val widthSinglePart = (width - tableConfig.columnConfigs.size) / weights
                val widths = tableConfig.columnConfigs.map { it.weight * widthSinglePart }

                val lastRange = tableData.items.takeLast(height - 1)

                var column = 0
                tableConfig.columnConfigs.forEachIndexed { columnIndex, columnConfig ->
                    val columnWidth = widths[columnIndex]
                    if (columnConfig is TableConfig.ColumnConfig.StringColumnConfig) {
                        val title =
                            if (columnConfig.title.length < columnWidth) {
                                columnConfig.title
                            } else if (!columnConfig.trimFromEnd) {
                                "…" + columnConfig.title.substring(0, columnWidth - 1)
                            } else {
                                columnConfig.title.substring(
                                    columnConfig.title.length - columnWidth - 1,
                                    columnConfig.title.length,
                                ) + "…"
                            }
                        drawText(
                            row = 0,
                            column = column,
                            string = title,
                            foreground = tableConfig.titleColor,
                        )
                    }

                    lastRange.forEachIndexed { index, item ->
                        when (columnConfig) {
                            is TableConfig.ColumnConfig.StringColumnConfig -> {
                                val string = columnConfig.stringFromItem(item)
                                val text =
                                    if (string.length < columnWidth) {
                                        string
                                    } else {
                                        string.substring(0, columnWidth)
                                    }
                                drawText(
                                    row = index + 1,
                                    column = if (columnConfig.valueAlignment.isStart) {
                                        column
                                    } else {
                                        column + columnWidth - text.length
                                    },
                                    string = text,
                                    foreground = if (index == selectedRow) {
                                        columnConfig.selectedValueColor
                                    } else {
                                        columnConfig.valueColor
                                    },
                                )
                            }
                            is TableConfig.ColumnConfig.ProgressColumnConfig -> {
                                drawText(
                                    row = index + 1,
                                    column = column,
                                    string = buildAnnotatedString {
                                        val progress = columnConfig.progressFromItem(item)
                                        val filledPart = (columnWidth * progress).roundToInt()
                                        withStyle(SpanStyle(columnConfig.filledColor)) {
                                            append("━".repeat(filledPart))
                                        }
                                        withStyle(SpanStyle(columnConfig.emptyColor)) {
                                            append("━".repeat(columnWidth - filledPart))
                                        }
                                    },
                                )
                            }
                        }
                    }
                    column += columnWidth + 1
                }
            },
    ) {
        Spacer(modifier = Modifier.fillMaxSize())
    }
}
