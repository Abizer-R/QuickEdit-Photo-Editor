package com.abizer_r.touchdraw.ui.effectsMode

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter

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

    BackHandler {
        onBackPressed()
    }

    var filteredBitmap by remember {
        mutableStateOf(bitmap)
    }

    LaunchedEffect(key1 = bitmap) {
        val gpuImage = GPUImage(context).apply {
            setImage(bitmap)
        }
//        context.assets.open("acv/blue_yellow_field.acv").apply {
//            val gpuFilter = GPUImageToneCurveFilter()
//            gpuFilter.setFromCurveFileInputStream(this)
//            close()
//            gpuImage.setFilter(gpuFilter)
//            filteredBitmap = gpuImage.bitmapWithFilterApplied
//        }

        gpuImage.setFilter(GPUImageGrayscaleFilter())
        filteredBitmap = gpuImage.bitmapWithFilterApplied
    }


    val onCloseClickedLambda = remember<() -> Unit> {
        {
            onBackPressed()
        }
    }

    val onDoneClickedLambda = remember<() -> Unit> {
        {
//        viewModel.handleStateBeforeCaptureScreenshot()
//        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
//            delay(200)  /* Delay to update the ToolbarExtensionView Visibility in ui */
//            screenshotState.capture()
//        }
        }
    }

    val screenshotState = rememberScreenshotState()

    when (screenshotState.imageState.value) {
        ImageResult.Initial -> {}
        is ImageResult.Error -> {
//            viewModel.shouldGoToNextScreen = false
//            context.defaultErrorToast()
        }

        is ImageResult.Success -> {
//            if (viewModel.shouldGoToNextScreen) {
//                viewModel.shouldGoToNextScreen = false
//                screenshotState.bitmap?.let { mBitmap ->
//                    onDoneClicked(mBitmap)
//                } ?: context.defaultErrorToast()
//            }
        }
    }


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        val (topToolBar, screenshotBox, filterListView) = createRefs()

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
//                    bottom.linkTo(filterListView.top)
                    bottom.linkTo(parent.bottom)
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
                bitmap = filteredBitmap.asImageBitmap(),
                contentDescription = null
            )

        }


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