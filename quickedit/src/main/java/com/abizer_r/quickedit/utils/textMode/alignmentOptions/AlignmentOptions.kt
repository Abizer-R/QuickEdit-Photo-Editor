package com.abizer_r.quickedit.utils.textMode.alignmentOptions

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.FormatAlignRight
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.QuickEditTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
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
    ) {
        optionList.forEachIndexed { index, textAlign ->
            AlignmentItem(
                textAlign = textAlign,
                itemSize = 24.dp,
                isSelected = textAlign == selectedAlignment,
                onClick = {
                    onItemClicked(index, it)
                }
            )
        }
    }
}

@Composable
fun AlignmentItem(
    modifier: Modifier = Modifier,
    textAlign: TextAlign,
    itemSize: Dp,
    isSelected: Boolean,
    onClick: (textAlign: TextAlign) -> Unit
) {
    val itemModifier = if (isSelected) {
        modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color.DarkGray)
            .padding(8.dp)
    } else {
        modifier.padding(8.dp)
    }
    val textAlignIcon = when (textAlign) {
        TextAlign.Left,
        TextAlign.Start -> {
            Icons.AutoMirrored.Outlined.FormatAlignLeft
        }
        TextAlign.Right,
        TextAlign.End -> {
            Icons.AutoMirrored.Outlined.FormatAlignRight

        }
        else -> {
            Icons.Default.FormatAlignCenter
        }
    }
    Image(
        imageVector = textAlignIcon,
        contentDescription = null,
        modifier = itemModifier
            .size(itemSize)
            .clickable {
                onClick(textAlign)
            },
        colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentItem() {
    QuickEditTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            AlignmentItem(
                textAlign = TextAlign.Center,
                itemSize = 24.dp,
                isSelected = true,
                onClick = {}
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