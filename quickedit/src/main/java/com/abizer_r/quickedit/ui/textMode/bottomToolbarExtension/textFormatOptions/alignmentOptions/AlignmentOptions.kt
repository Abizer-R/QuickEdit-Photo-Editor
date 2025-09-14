package com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.alignmentOptions

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.FormatAlignRight
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.abizerr.quickedit.ui.theme.QuickEditTheme
import io.github.abizerr.quickedit.ui.theme.ToolBarBackgroundColor
import com.abizer_r.quickedit.ui.common.toolbar.SelectableToolbarItem
import com.abizer_r.quickedit.utils.textMode.TextModeUtils

@Composable
fun TextAlignOptions(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ToolBarBackgroundColor,
    selectedAlignment: TextAlign = TextModeUtils.DEFAULT_TEXT_ALIGN,
    optionList: ArrayList<TextAlign> = TextModeUtils.getTextAlignOptions(),
    onItemClicked: (position: Int, textAlign: TextAlign) -> Unit
) {

    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        optionList.forEachIndexed { index, textAlign ->

            val textAlignIcon = when (textAlign) {
                TextAlign.Left,TextAlign.Start ->
                    Icons.AutoMirrored.Outlined.FormatAlignLeft

                TextAlign.Right, TextAlign.End ->
                    Icons.AutoMirrored.Outlined.FormatAlignRight

                else -> Icons.Default.FormatAlignCenter
            }

            SelectableToolbarItem(
                imageVector = textAlignIcon,
                isSelected = textAlign == selectedAlignment,
                onClick = {

                    onItemClicked(index, textAlign)
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions() {
    QuickEditTheme {
        TextAlignOptions(
            selectedAlignment = TextAlign.Center,
            onItemClicked = { _, _ -> }
        )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions_FullWidth() {
    QuickEditTheme {
        TextAlignOptions(
            modifier = Modifier.fillMaxWidth(),
            selectedAlignment = TextAlign.Center,
            onItemClicked = { _, _ -> }
        )

    }
}