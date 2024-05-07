package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvents
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.utils.DrawingUtils

@Composable
fun BottomToolBar(
    modifier: Modifier,
    bottomToolbarState: BottomToolbarState,
    onEvent: (BottomToolbarEvents) -> Unit
) {

    LazyRow(
        modifier = modifier.background(ToolBarBackgroundColor)
    ) {
        items(bottomToolbarState.toolbarItems) { mToolbarItem ->
            ToolbarItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                toolbarItem = mToolbarItem,
                selectedColor = bottomToolbarState.selectedColor,
                isSelected = mToolbarItem == bottomToolbarState.selectedItem,
                onEvent = onEvent
            )
        }
    }
}


@Composable
fun ToolbarItem(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    toolbarItem: BottomToolbarItem,
    isSelected: Boolean,
    onEvent: (BottomToolbarEvents) -> Unit
) {
    if (toolbarItem is BottomToolbarItem.ColorItem) {
        ColorToolbarItem(
            modifier = modifier,
            selectedColor = selectedColor,
            colorItem = toolbarItem,
            onEvent = onEvent
        )
        return
    }
    val columnModifier = if (isSelected) {
        modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color.DarkGray)
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
    } else {
        modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
    }

    val (imageVector, labelText) = when (toolbarItem) {
        is BottomToolbarItem.EraserTool -> Pair(
            ImageVector.vectorResource(id = R.drawable.ic_eraser),
            stringResource(id = R.string.tool_label_Eraser)
        )

        is BottomToolbarItem.ShapeTool -> Pair(
            Icons.Default.Category,
            stringResource(id = R.string.tool_label_Shape)
        )

        else -> Pair(
            Icons.Default.Brush,
            stringResource(id = R.string.tool_label_brush)
        )
    }


    Column(
        modifier = columnModifier.clickable {
            onEvent(BottomToolbarEvents.OnItemClicked(toolbarItem))
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSelected) {
            Image(
                modifier = Modifier
                    .size(16.dp)
                    .scale(1.5f),
                contentDescription = null,
                imageVector = Icons.Default.ArrowDropUp,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        } else {
            Spacer(modifier = Modifier.size(16.dp))
        }

        Image(
            modifier = Modifier.size(24.dp),
            contentDescription = null,
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Text(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            ),
            text = labelText
        )
    }
}

@Composable
fun ColorToolbarItem(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    colorItem: BottomToolbarItem.ColorItem,
    onEvent: (BottomToolbarEvents) -> Unit
) {
    Column(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = ColorPainter(selectedColor),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.onBackground)
                .padding(1.dp)
                .clip(CircleShape)
                .size(23.dp)
                .clickable {
                    onEvent(BottomToolbarEvents.OnItemClicked(colorItem))
                }
        )

        Text(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            text = stringResource(id = R.string.tool_label_color)
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBottomToolbar() {
    SketchDraftTheme {
        BottomToolBar(
            modifier = Modifier.fillMaxWidth(),
            bottomToolbarState = DrawingUtils.getDefaultBottomToolbarState(),
            onEvent = {}
        )
    }
}