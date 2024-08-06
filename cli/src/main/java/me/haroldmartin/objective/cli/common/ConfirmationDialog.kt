package me.haroldmartin.objective.cli.common

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.fillMaxSize
import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text

private const val MSG_LINE_LIMIT = 80

@Composable
fun ConfirmationDialog(
    title: String,
    messages: List<String>,
    titleColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.border(color = borderColor).padding(2).align(Alignment.Center),
        ) {
            Text(
                title,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(2),
                color = titleColor,
            )
            messages.map {
                Text(
                    if (it.length > MSG_LINE_LIMIT) {
                        it.substring(
                            0,
                            MSG_LINE_LIMIT - 1,
                        ) + "…"
                    } else {
                        it
                    },
                    color = titleColor,
                )
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(2)) {
                Text("Press ", color = borderColor)
                Text("y", color = titleColor)
                Text(" to confirm or ", color = borderColor)
                Text("n", color = titleColor)
                Text(" to cancel", color = borderColor)
            }
        }
    }
}
