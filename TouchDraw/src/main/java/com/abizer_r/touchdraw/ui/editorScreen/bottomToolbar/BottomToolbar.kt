package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.TextFields
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
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.utils.drawMode.DrawModeUtils
import com.abizer_r.touchdraw.utils.drawMode.DrawingConstants
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils

@Composable
fun BottomToolBar(
    modifier: Modifier,
    bottomToolbarState: BottomToolbarState,
    onEvent: (BottomToolbarEvent) -> Unit
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(ToolBarBackgroundColor)
    ) {
        itemsIndexed(bottomToolbarState.toolbarItems) { index, mToolbarItem ->
            val itemModifier = Modifier
            if (index == 0) {
                itemModifier.padding(start = 8.dp)
            } else if (index == bottomToolbarState.toolbarItems.size - 1) {
                itemModifier.padding(end = 8.dp)
            } else {
                itemModifier.padding(horizontal = 16.dp)
            }
            ToolbarItem(
                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),
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
    onEvent: (BottomToolbarEvent) -> Unit
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
            stringResource(id = R.string.eraser)
        )

        is BottomToolbarItem.ShapeTool -> Pair(
            Icons.Default.Category,
            stringResource(id = R.string.shape)
        )

        is BottomToolbarItem.TextMode -> Pair(
            Icons.Default.TextFields,
            stringResource(id = R.string.text)
        )

        is BottomToolbarItem.BrushTool -> Pair(
            ImageVector.vectorResource(id = R.drawable.ic_stylus_note),
            stringResource(id = R.string.brush)
        )

        else -> Pair(
            Icons.Default.AddCircleOutline,
            ""
        )
    }


    Column(
        modifier = columnModifier.clickable {
            onEvent(BottomToolbarEvent.OnItemClicked(toolbarItem))
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSelected) {
            Image(
                modifier = Modifier
                    .size(height = 12.dp, width = 24.dp)
                    .scale(1f),
                contentDescription = null,
                imageVector = Icons.Default.ArrowDropDown,
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        } else {
            Spacer(modifier = Modifier.size(
                if (labelText.isNotBlank()) 12.dp else 4.dp
            ))
        }

        val imageSize = if (labelText.isNotBlank()) 24.dp else 32.dp
        Image(
            modifier = Modifier.size(imageSize),
            contentDescription = null,
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        if (labelText.isNotBlank()) {
            Text(
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                ),
                text = labelText
            )
        }
    }
}

@Composable
fun ColorToolbarItem(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    colorItem: BottomToolbarItem.ColorItem,
    onEvent: (BottomToolbarEvent) -> Unit
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
                    onEvent(BottomToolbarEvent.OnItemClicked(colorItem))
                }
        )

        Text(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            text = stringResource(id = R.string.color)
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DrawMode_BottomToolbar() {
    SketchDraftTheme {
        BottomToolBar(
            modifier = Modifier.fillMaxWidth(),
            bottomToolbarState = DrawModeUtils.getDefaultBottomToolbarState(),
            onEvent = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TextMode_BottomToolbar() {
    SketchDraftTheme {
        BottomToolBar(
            modifier = Modifier.fillMaxWidth(),
            bottomToolbarState = TextModeUtils.getDefaultBottomToolbarState(),
            onEvent = {}
        )
    }
}