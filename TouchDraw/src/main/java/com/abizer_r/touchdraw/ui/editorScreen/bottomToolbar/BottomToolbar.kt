package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.utils.drawMode.DrawModeUtils
import com.abizer_r.touchdraw.utils.editorScreen.EditorScreenUtils
import com.abizer_r.touchdraw.utils.editorScreen.EffectsModeUtils
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils

@Composable
fun BottomToolBar(
    modifier: Modifier,
    toolbarItems: ImmutableList<BottomToolbarItem>,
    selectedItem: BottomToolbarItem = BottomToolbarItem.NONE,
    showColorPickerIcon: Boolean = true,
    selectedColor: Color = Color.White,
    onEvent: (BottomToolbarEvent) -> Unit
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(ToolBarBackgroundColor)
    ) {
        itemsIndexed(toolbarItems.items) { index, mToolbarItem ->
            val itemModifier = Modifier
            if (index == 0) {
                itemModifier.padding(start = 8.dp)
            } else if (index == toolbarItems.items.size - 1) {
                itemModifier.padding(end = 8.dp)
            } else {
                itemModifier.padding(horizontal = 16.dp)
            }
            ToolbarItem(
                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),
                toolbarItem = mToolbarItem,
                selectedColor = selectedColor,
                showColorPickerIcon = showColorPickerIcon,
                isSelected = mToolbarItem == selectedItem,
                onEvent = onEvent
            )
        }
    }
}


@Composable
fun ToolbarItem(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    showColorPickerIcon: Boolean,
    toolbarItem: BottomToolbarItem,
    isSelected: Boolean,
    onEvent: (BottomToolbarEvent) -> Unit
) {
    val labelFontSize = MaterialTheme.typography.bodySmall.fontSize

    if (toolbarItem is BottomToolbarItem.ColorItem) {
        ColorToolbarItem(
            modifier = modifier,
            selectedColor = selectedColor,
            showColorPickerIcon = showColorPickerIcon,
            colorItem = toolbarItem,
            labelFontSize = labelFontSize,
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

        is BottomToolbarItem.DrawMode -> Pair(
            Icons.Outlined.Brush,
            stringResource(id = R.string.draw)

        )
        is BottomToolbarItem.TextMode -> Pair(
            Icons.Default.TextFields,
            stringResource(id = R.string.text)
        )
        is BottomToolbarItem.EffectsMode -> Pair(
            ImageVector.vectorResource(id = com.abizer_r.touchdraw.R.drawable.ic_effects),
            stringResource(id = R.string.effects)
        )

        is BottomToolbarItem.EraserTool -> Pair(
            ImageVector.vectorResource(id = R.drawable.ic_eraser),
            stringResource(id = R.string.eraser)
        )

        is BottomToolbarItem.ShapeTool -> Pair(
            Icons.Outlined.Category,
            stringResource(id = R.string.shape)
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

    val paddingAfterSize = when (toolbarItem) {
        is BottomToolbarItem.EffectsMode -> 1.dp
        else -> 0.dp
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

        val verticalPaddingBeforeSize = if (labelText.isBlank()) 8.dp else 0.dp
        val imageSize = if (labelText.isNotBlank()) 28.dp else 36.dp
        Image(
            modifier = Modifier
                .padding(vertical = verticalPaddingBeforeSize)
                .size(imageSize)
                .padding(paddingAfterSize),
            contentDescription = null,
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.size(
            if (labelText.isNotBlank()) 4.dp else 0.dp
        ))

        if (labelText.isNotBlank()) {
            Text(
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = labelFontSize,
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
    showColorPickerIcon: Boolean,
    colorItem: BottomToolbarItem.ColorItem,
    labelFontSize: TextUnit,
    onEvent: (BottomToolbarEvent) -> Unit
) {
    Column(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (showColorPickerIcon) {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.ic_color_picker),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onEvent(BottomToolbarEvent.OnItemClicked(colorItem))
                    }
            )
        } else {
            Image(
                painter = ColorPainter(selectedColor),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.onBackground)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .size(26.dp)
                    .clickable {
                        onEvent(BottomToolbarEvent.OnItemClicked(colorItem))
                    }
            )
        }
        

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = labelFontSize
            ),
            text = stringResource(id = R.string.color)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditorScreen_BottomToolbar() {
    SketchDraftTheme {
        val itemsList = EditorScreenUtils.getDefaultBottomToolbarItemsList()
        BottomToolBar(
            modifier = Modifier.fillMaxWidth(),
            toolbarItems = ImmutableList(itemsList),
            onEvent = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DrawMode_BottomToolbar() {
    SketchDraftTheme {
        val itemsList = DrawModeUtils.getDefaultBottomToolbarItemsList()
        BottomToolBar(
            modifier = Modifier.fillMaxWidth(),
            toolbarItems = ImmutableList(itemsList),
            showColorPickerIcon = true,
            selectedColor = Color.White,
            selectedItem = itemsList[1],
            onEvent = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TextMode_BottomToolbar() {
    SketchDraftTheme {
        val itemsList = TextModeUtils.getDefaultBottomToolbarItemsList()
        BottomToolBar(
            modifier = Modifier.fillMaxWidth(),
            toolbarItems = ImmutableList(itemsList),
            showColorPickerIcon = true,
            selectedColor = Color.White,
            onEvent = {}
        )
    }
}