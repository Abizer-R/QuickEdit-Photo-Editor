package com.abizer_r.components.ui.tool_items

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.components.util.ColorUtils
import com.abizer_r.components.util.ImmutableList

/**
 * The total size of this item is the adding of [itemSize] and [selectedBorderWidth]
 * @param itemSize
 * @param selectedBorderWidth
 */
@Composable
fun ColorListFullWidth(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ToolBarBackgroundColor,
    colorList: ImmutableList<Color>,
    selectedIndex: Int,
    onItemClicked: (position: Int, color: Color) -> Unit
) {

    val scrollState = rememberScrollState()
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .horizontalScroll(scrollState),
    ) {
        colorList.items.forEachIndexed { index, color ->
            SelectableColor(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                itemColor = color,
                itemSize = 32.dp,
                isSelected = index == selectedIndex,
                selectedBorderWidth = 2.dp,
                selectedBorderColor = MaterialTheme.colorScheme.onBackground,
                clipShape = CircleShape,
                onClick = {
                    onItemClicked(index, it)
                }
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_ColorList() {
    SketchDraftTheme {
        ColorListFullWidth(
            colorList = ImmutableList(ColorUtils.defaultColorList),
            selectedIndex = 2,
            onItemClicked = { _, _ -> }
        )

    }
}