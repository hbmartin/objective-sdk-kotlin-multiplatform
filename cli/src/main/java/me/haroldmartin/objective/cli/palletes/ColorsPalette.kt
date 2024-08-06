package me.haroldmartin.objective.cli.palletes

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.jakewharton.mosaic.ui.Color

val LocalColorsPalette: ProvidableCompositionLocal<ColorsPalette> =
    staticCompositionLocalOf { throw IllegalStateException("No colors palette") }

fun parseArgsForColorsPalette(args: Array<String>): ColorsPalette {
    val keyIndex = args.indexOf("-c")
    if (keyIndex < 0 || keyIndex >= args.lastIndex) {
        return DefaultColorsPalette
    }
    return when (args[keyIndex + 1]) {
        "default" -> DefaultColorsPalette
        "blackbird" -> BlackBirdColorsPalette
        "dracula" -> DraculaColorsPalette
        "nord" -> NordColorsPalette
        "one-dark" -> OneDarkColorsPalette
        "solarized-dark" -> SolarizedDarkColorsPalette
        else -> DefaultColorsPalette
    }
}

@Immutable
data class ColorsPalette(
    val mainBg: Color,
    val menuBg: Color,
    val menuFg: Color,
    val menuDividerFg: Color,
    val menuHighlightFg: Color,
    val statusBarFg: Color,
    val memoryTitleFg: Color,
    val memoryBorderFg: Color,
    val memoryMaxMemoryTextFg: Color,
    val memoryUsedMemoryTextFg: Color,
    val memoryUsedMemorySparklineFg: Color,
    val memoryUsedMemorySparklineBaselineFg: Color,
    val memoryFragRatioTextFg: Color,
    val memoryRssMemoryTextFg: Color,
    val memoryRssMemorySparklineFg: Color,
    val memoryRssMemorySparklineBaselineFg: Color,
    val cpuTitleFg: Color,
    val cpuBorderFg: Color,
    val cpuChartLineFg: Color,
    val cpuChartAxisFg: Color,
    val cpuSysCpuText1Fg: Color,
    val cpuSysCpuText2Fg: Color,
    val cpuSysCpuDatasetFg: Color,
    val cpuUserCpuText1Fg: Color,
    val cpuUserCpuText2Fg: Color,
    val cpuUserCpuDatasetFg: Color,
    val throughputTitleFg: Color,
    val throughputBorderFg: Color,
    val throughputTotalCommandsTextFg: Color,
    val throughputOpsTextFg: Color,
    val throughputSparklineFg: Color,
    val throughputSparklineBaselineFg: Color,
    val networkTitleFg: Color,
    val networkBorderFg: Color,
    val dangerBg: Color,
    val networkRxSTextFg: Color,
    val networkRxSparklineFg: Color,
    val networkRxSparklineBaselineFg: Color,
    val networkTxTotalTextFg: Color,
    val networkTxSTextFg: Color,
    val networkTxSparklineFg: Color,
    val networkTxSparklineBaselineFg: Color,
    val statTitleFg: Color,
    val statBorderFg: Color,
    val statTableHeaderFg: Color,
    val statTableRowGaugeFg: Color,
    val statTableRowGaugeBg: Color,
    val statTableRowTop1Fg: Color,
    val statTableRowTop2Fg: Color,
    val statTableRowHighlightBg: Color,
    val callsTitleFg: Color,
    val callsBorderFg: Color,
    val callsTableHeaderFg: Color,
    val callsTableRowGaugeFg: Color,
    val callsTableRowGaugeBg: Color,
    val callsTableRowTop1Fg: Color,
    val callsTableRowTop2Fg: Color,
    val callsTableRowHighlightBg: Color,
    val rawTitleFg: Color,
    val rawBorderFg: Color,
    val rawTableHeaderFg: Color,
    val rawTableRowTop1Fg: Color,
    val rawTableRowTop2Fg: Color,
    val rawTableRowHighlightBg: Color,
    val slowTitleFg: Color,
    val slowBorderFg: Color,
    val slowTableHeaderFg: Color,
    val slowTableRowTop1Fg: Color,
    val slowTableRowTop2Fg: Color,
    val slowTableRowHighlightBg: Color,
)
