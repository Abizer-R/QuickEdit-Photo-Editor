package com.abizer_r.touchdraw.ui.effectsMode.effectsPreview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abizer_r.components.R
import com.abizer_r.components.theme.Black_alpha_30
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.touchdraw.utils.editorScreen.EffectsModeUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EffectsPreviewListFullWidth(
    modifier: Modifier = Modifier,
    effectsList: ImmutableList<EffectItem>,
    selectedIndex: Int,
    onItemClicked: (position: Int, effectItem: EffectItem) -> Unit
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            count = effectsList.items.size,
            key = { it },
        ) {index ->
            val effectItem = effectsList.items[index]
            EffectPreview(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 4.dp),
                effectItem = effectItem,
                isSelected = index == selectedIndex,
                selectedBorderColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    onItemClicked(index, it)
                }
            )
        }
    }
}

@Composable
fun EffectPreview(
    modifier: Modifier = Modifier,
    effectItem: EffectItem,
    isSelected: Boolean,
    itemSize: Dp = 84.dp,
    selectedBorderWidth: Dp = 1.dp,
    selectedBorderColor: Color = Color.White,
    clipShape: Shape = RectangleShape,
    onClick: (effectItem: EffectItem) -> Unit
) {

    val borderColor = if (isSelected) selectedBorderColor else Color.Transparent
    Box(
        modifier = modifier
            .clip(clipShape)
            .background(color = borderColor)
            .padding(selectedBorderWidth)
            .clip(clipShape)
            .size(itemSize)
            .clickable {
                onClick(effectItem)
            }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = effectItem.previewBitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .background(Black_alpha_30)
                .align(Alignment.BottomCenter),
            text = effectItem.label,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 9.sp,
            ),
        )
    }

}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Selected_EffectPreviewItem() {
    SketchDraftTheme {
        EffectPreview(
            modifier = Modifier.padding(8.dp),
            effectItem = EffectItem(
                previewBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1).asAndroidBitmap(),
                label = "Dummy Effect"
            ),
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Unselected_EffectPreviewItem() {
    SketchDraftTheme {
        EffectPreview(
            modifier = Modifier.padding(8.dp),
            effectItem = EffectItem(
                previewBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1).asAndroidBitmap(),
                label = "Dummy Effect"
            ),
            isSelected = false,
            onClick = {}
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_EffectsPreviewList() {
    val mEffectsList = arrayListOf(
        EffectItem(
            previewBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1).asAndroidBitmap(),
            label = "original"
        ),
        EffectItem(
            previewBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1).asAndroidBitmap(),
            label = "greyscale"
        ),
        EffectItem(
            previewBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1).asAndroidBitmap(),
            label = "poppy dogs"
        )
    )
    SketchDraftTheme {
        EffectsPreviewListFullWidth(
            modifier = Modifier
                .background(ToolBarBackgroundColor)
                .padding(vertical = 12.dp),
            effectsList = ImmutableList(mEffectsList),
            selectedIndex = 0,
            onItemClicked = {_, _ ->}
        )
    }
}