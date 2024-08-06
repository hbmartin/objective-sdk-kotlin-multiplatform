package me.haroldmartin.objective.cli.common

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.layout.widthIn
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.text.SpanStyle
import com.jakewharton.mosaic.text.buildAnnotatedString
import com.jakewharton.mosaic.text.withStyle
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    titleColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .border(color = borderColor)
                .padding(horizontal = 1)
                .widthIn(max = 80),
    ) {
        Text(
            buildAnnotatedString {
                append(" ¡ ")
                withStyle(SpanStyle(titleColor)) {
                    append(title)
                }
                append(" ! ")
            },
            modifier = Modifier.align(Alignment.TopCenter),
            color = borderColor,
        )
        Text(message, modifier = Modifier.align(Alignment.Center))
        Row {
            Text("Press ", color = borderColor)
            Text("y", color = titleColor)
            Text(" to confirm or ", color = borderColor)
            Text("n", color = titleColor)
            Text(" to cancel", color = borderColor)
        }
    }
}
