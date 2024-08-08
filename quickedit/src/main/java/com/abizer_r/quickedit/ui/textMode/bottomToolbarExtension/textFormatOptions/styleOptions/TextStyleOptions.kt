package com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.theme.ToolBarBackgroundColor
import com.abizer_r.quickedit.ui.common.toolbar.SelectableToolbarItem

@Composable
fun TextStyleOptions(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ToolBarBackgroundColor,
    textStyleAttr: TextStyleAttr,
    showDividers: Boolean = false,
    onItemClicked: (newTextStyleAttr: TextStyleAttr) -> Unit
) {

    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        SelectableToolbarItem(
            imageVector = Icons.Default.FormatBold,
            isSelected = textStyleAttr.isBold,
            onClick = {
                onItemClicked(
                    textStyleAttr.copy(isBold = !textStyleAttr.isBold)
                )
            }
        )

        if (showDividers) {
            VerticalDivider(Modifier.height(24.dp))
        }


        SelectableToolbarItem(
            imageVector = Icons.Default.FormatItalic,
            isSelected = textStyleAttr.isItalic,
            onClick = {
                onItemClicked(
                    textStyleAttr.copy(isItalic = !textStyleAttr.isItalic)
                )
            }
        )

        if (showDividers) {
            VerticalDivider(Modifier.height(24.dp))
        }

        SelectableToolbarItem(
            imageVector = Icons.Default.FormatUnderlined,
            isSelected = textStyleAttr.isUnderline,
            onClick = {
                onItemClicked(
                    textStyleAttr.copy(isUnderline = !textStyleAttr.isUnderline)
                )
            }
        )

        if (showDividers) {
            VerticalDivider(Modifier.height(24.dp))
        }

        SelectableToolbarItem(
            imageVector = Icons.Default.FormatStrikethrough,
            isSelected = textStyleAttr.isStrikeThrough,
            onClick = {
                onItemClicked(
                    textStyleAttr.copy(isStrikeThrough = !textStyleAttr.isStrikeThrough)
                )
            }
        )

    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions() {
    QuickEditTheme {
        TextStyleOptions(
            textStyleAttr = TextStyleAttr(
                isBold = true,
                isUnderline = true
            ),
            onItemClicked = {}
        )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions_FullWidth() {
    QuickEditTheme {
        TextStyleOptions(
            modifier = Modifier.fillMaxWidth(),
            showDividers = true,
            textStyleAttr = TextStyleAttr(
                isBold = true,
                isUnderline = true
            ),
            onItemClicked = {}
        )

    }
}
