package com.abizer_r.touchdraw.ui.effectsMode

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.touchdraw.ui.effectsMode.effectsPreview.EffectItem
import com.abizer_r.touchdraw.ui.effectsMode.effectsPreview.EffectsPreviewListFullWidth
import com.abizer_r.touchdraw.ui.textMode.TextModeEvent
import com.abizer_r.touchdraw.ui.textMode.TextModeViewModel
import com.abizer_r.touchdraw.utils.editorScreen.EffectsModeUtils
import com.abizer_r.touchdraw.utils.textMode.colorList.ColorListFullWidth
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EffectsModeScreen(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    onDoneClicked: (bitmap: Bitmap) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val viewModel: EffectsModeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    val currentBitmap = state.filteredBitmap ?: bitmap

    BackHandler {
        onBackPressed()
    }

    LaunchedEffect(key1 = bitmap) {
        viewModel.updateEffectList(
            effectList = EffectsModeUtils.getEffectsPreviewList(context, bitmap)
        )
        viewModel.selectEffect(0)
    }

    val screenshotState = rememberScreenshotState()

    when (screenshotState.imageState.value) {
        ImageResult.Initial -> {}
        is ImageResult.Error -> {
            viewModel.shouldGoToNextScreen = false
            context.defaultErrorToast()
        }

        is ImageResult.Success -> {
            if (viewModel.shouldGoToNextScreen) {
                viewModel.shouldGoToNextScreen = false
                screenshotState.bitmap?.let { mBitmap ->
                    onDoneClicked(mBitmap)
                } ?: context.defaultErrorToast()
            }
        }
    }

    val onCloseClickedLambda = remember<() -> Unit> { {
        onBackPressed()
    }}

    val onDoneClickedLambda = remember<() -> Unit> { {
        viewModel.shouldGoToNextScreen = true
        screenshotState.capture()
    }}


    val onEffectItemClicked = remember<(Int, EffectItem) -> Unit> {{ index, effectItem ->
        viewModel.selectEffect(index)
    }}


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        val (topToolBar, screenshotBox, effectsPreviewList) = createRefs()

        TextModeTopToolbar(
            modifier = Modifier.constrainAs(topToolBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            onCloseClicked = onCloseClickedLambda,
            onDoneClicked = onDoneClickedLambda
        )
        val aspectRatio = bitmap.let {
            bitmap.width.toFloat() / bitmap.height.toFloat()
        }
        ScreenshotBox(
            modifier = Modifier
                .constrainAs(screenshotBox) {
                    top.linkTo(topToolBar.bottom)
                    bottom.linkTo(effectsPreviewList.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.ratio(aspectRatio.toString())
                    height = Dimension.fillToConstraints
                }
                .clipToBounds(),
            screenshotState = screenshotState
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = currentBitmap.asImageBitmap(),
                contentDescription = null
            )

        }

        EffectsPreviewListFullWidth(
            modifier = Modifier.constrainAs(effectsPreviewList) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            }
                .background(ToolBarBackgroundColor)
                .padding(vertical = 12.dp),
            effectsList = ImmutableList(state.effectsList),
            selectedIndex = state.selectedEffectIndex,
            onItemClicked = onEffectItemClicked
        )


    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_EffectsModeScreen() {
    SketchDraftTheme {
        EffectsModeScreen(
            bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_2)
                .asAndroidBitmap(),
            onDoneClicked = {},
            onBackPressed = {}
        )
    }
}