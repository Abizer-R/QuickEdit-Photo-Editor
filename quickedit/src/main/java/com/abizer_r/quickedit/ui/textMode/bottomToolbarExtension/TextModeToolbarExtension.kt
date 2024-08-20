package com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.theme.ToolBarBackgroundColor
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.TextModeToolbarExtensionEvent.*
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.fontFamilyOptions.FontFamilyOptions
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.caseOptions.TextCaseType
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.alignmentOptions.TextAlignOptions
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.caseOptions.TextCaseOptions
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions.TextStyleOptions
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions.TextStyleAttr
import com.abizer_r.quickedit.utils.drawMode.DrawModeUtils
import com.abizer_r.quickedit.utils.textMode.FontUtils
import com.abizer_r.quickedit.utils.textMode.TextModeUtils

/**
 * Contains additional option for text-mode tools
 */
@Composable
fun TextModeToolbarExtension(
    modifier: Modifier,
    bottomToolbarItem: BottomToolbarItem,
    onEvent: (TextModeToolbarExtensionEvent) -> Unit
) {

    when (bottomToolbarItem) {
        is BottomToolbarItem.TextFormat -> {
            TextModeToolbarExtTextFormat(
                modifier = modifier,
                bottomToolbarItem = bottomToolbarItem,
                onEvent = onEvent
            )
        }

        is BottomToolbarItem.TextFontFamily -> {
            TextModeToolbarExtFont(
                modifier = modifier,
                bottomToolbarItem = bottomToolbarItem,
                onEvent = onEvent
            )
        }

        else -> {}
    }

}

@Composable
fun TextModeToolbarExtFont(
    modifier: Modifier = Modifier,
    bottomToolbarItem: BottomToolbarItem.TextFontFamily,
    onEvent: (TextModeToolbarExtensionEvent) -> Unit
) {

    FontFamilyOptions(
        modifier = modifier,
        selectedFontFamily = bottomToolbarItem.textFontFamily,
        onItemClicked = {
            onEvent(UpdateTextFontFamily(it))
        }
    )
}


@Composable
fun TextModeToolbarExtTextFormat(
    modifier: Modifier = Modifier,
    bottomToolbarItem: BottomToolbarItem.TextFormat,
    onEvent: (TextModeToolbarExtensionEvent) -> Unit
) {
    Column(
        modifier = modifier
            .background(ToolBarBackgroundColor)
    ) {

        TextStyleOptions(
            modifier = Modifier.fillMaxWidth(),
            textStyleAttr = bottomToolbarItem.textStyleAttr,
            showDividers = true,
            onItemClicked = {
                onEvent(
                    UpdateTextStyleAttr(textStyleAttr = it)
                )
            }
        )

        HorizontalDivider(Modifier.padding(4.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextCaseOptions(
                modifier = Modifier.weight(1f),
                selectedTextCase = bottomToolbarItem.textCaseType,
                onItemClicked = {
                    onEvent(
                        UpdateTextCaseType(textCaseType = it)
                    )
                }
            )

            VerticalDivider(Modifier.height(24.dp))

            TextAlignOptions(
                modifier = Modifier.weight(1f),
                selectedAlignment = bottomToolbarItem.textAlign,
                onItemClicked = { _, textAlign ->
                    onEvent(
                        UpdateTextAlignment(textAlignment = textAlign)
                    )
                }
            )
        }
    }
}



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextModeToolbarExtension() {
    QuickEditTheme {
        val bottomTools = DrawModeUtils.getDefaultBottomToolbarItemsList()
        TextModeToolbarExtension(
            modifier = Modifier.fillMaxWidth(),
            bottomToolbarItem = BottomToolbarItem.TextFormat(
                textStyleAttr = TextStyleAttr(isBold = true),
                textCaseType = TextCaseType.DEFAULT,
                textAlign = TextModeUtils.DEFAULT_TEXT_ALIGN
            ),
            onEvent = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewToolbarExtFontFamily() {
    QuickEditTheme {
        val bottomTools = DrawModeUtils.getDefaultBottomToolbarItemsList()
        TextModeToolbarExtension(
            modifier = Modifier.fillMaxWidth(),
            bottomToolbarItem = BottomToolbarItem.TextFontFamily(
                textFontFamily = FontUtils.DefaultFontFamily
            ),
            onEvent = {}
        )
    }
}