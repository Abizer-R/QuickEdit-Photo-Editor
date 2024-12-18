package com.abizer_r.quickedit.ui.cropMode.cropperOptions

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.theme.ToolBarBackgroundColor
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_LARGE
import com.abizer_r.quickedit.utils.defaultTextColor
import com.abizer_r.quickedit.utils.editorScreen.CropModeUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CropperOptionsFullWidth(
    modifier: Modifier = Modifier,
    toolbarHeight: Dp = TOOLBAR_HEIGHT_LARGE,
    cropperOptionList: ArrayList<CropperOption>,
    selectedIndex: Int,
    onItemClicked: (position: Int, effectItem: CropperOption) -> Unit
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(toolbarHeight)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            count = cropperOptionList.size,
            key = { cropperOptionList[it].id },
        ) { index ->
            val cropperOption = cropperOptionList[index]
            CropperOptionView(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 4.dp),
                cropperOption = cropperOption,
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
fun CropperOptionView(
    modifier: Modifier = Modifier,
    cropperOption: CropperOption,
    isSelected: Boolean,
    selectedBorderWidth: Dp = 1.dp,
    selectedBorderColor: Color = Color.White,
    clipShape: Shape = RoundedCornerShape(4.dp),
    onClick: (cropperOption: CropperOption) -> Unit
) {

    val borderColor = if (isSelected) selectedBorderColor else Color.Transparent
    Column(
        modifier = modifier
            .wrapContentSize()
            .clip(clipShape)
            .background(color = borderColor)
            .padding(selectedBorderWidth)
            .clip(clipShape)
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
            .clickable {
                onClick(cropperOption)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp, bottom = 4.dp)
                .size(24.dp)
        ) {
            if (cropperOption.aspectRatioX == -1f) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = ImageVector.vectorResource(id = com.abizer_r.quickedit.R.drawable.baseline_crop_free_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            } else if (cropperOption.aspectRatioX == -2f) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = ImageVector.vectorResource(id = com.abizer_r.quickedit.R.drawable.ic_crop),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            } else {
                val ratio = cropperOption.aspectRatioX / cropperOption.aspectRatioY
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .aspectRatio(ratio)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color = MaterialTheme.colorScheme.onBackground)
                        .padding(1.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.background)
                )

            }
        }

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = cropperOption.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = defaultTextColor(),
                fontSize = 9.sp,
            ),
        )
    }

}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Selected_EffectPreviewItem() {
    QuickEditTheme {
        CropperOptionView(
            modifier = Modifier.padding(8.dp),
            cropperOption = CropperOption(
                aspectRatioX = 1f,
                aspectRatioY = 1f,
                label = "Square"
            ),
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Unselected_EffectPreviewItem() {
    QuickEditTheme {
        CropperOptionView(
            modifier = Modifier.padding(8.dp),
            cropperOption = CropperOption(
                aspectRatioX = 1f,
                aspectRatioY = 1f,
                label = "1:1"
            ),
            isSelected = false,
            onClick = {}
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_EffectsPreviewList() {
    QuickEditTheme {
        CropperOptionsFullWidth(
            modifier = Modifier
                .background(ToolBarBackgroundColor)
                .padding(vertical = 12.dp),
            cropperOptionList = CropModeUtils.getCropperOptionsList(),
            selectedIndex = 0,
            onItemClicked = {_, _ ->}
        )
    }
}