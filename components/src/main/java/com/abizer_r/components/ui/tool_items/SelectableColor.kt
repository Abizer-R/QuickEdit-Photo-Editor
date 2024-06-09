package com.abizer_r.components.ui.tool_items

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme

/**
 * The total size of this item is the adding of [itemSize] and [selectedBorderWidth]
 * @param itemSize
 * @param selectedBorderWidth
 */
@Composable
fun SelectableColor(
    modifier: Modifier = Modifier,
    itemColor: Color,
    itemSize: Dp,
    isSelected: Boolean,
    selectedBorderWidth: Dp = 2.dp,
    selectedBorderColor: Color = Color.White,
    clipShape: RoundedCornerShape = CircleShape,
    onClick: (color: Color) -> Unit
) {

    val borderColor = if (isSelected) selectedBorderColor else itemColor
    Image(
        painter = ColorPainter(itemColor),
        contentDescription = null,
        modifier = modifier
            .clip(clipShape)
            .background(color = borderColor)
            .padding(selectedBorderWidth)
            .clip(clipShape)
            .size(itemSize)
            .clickable {
                onClick(itemColor)
            }
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_SelectableColor() {
    SketchDraftTheme {
        SelectableColor(
            modifier = Modifier.padding(8.dp),
            itemColor = Color.Yellow,
            itemSize = 24.dp,
            isSelected = false,
            selectedBorderColor = Color.White,
            onClick = {}
        )
    }
}