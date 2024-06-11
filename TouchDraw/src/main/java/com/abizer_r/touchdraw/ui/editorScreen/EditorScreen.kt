package com.abizer_r.touchdraw.ui.editorScreen

import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.touchdraw.ui.drawMode.DrawModeViewModel
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.touchdraw.ui.textMode.TextModeEvent
import com.abizer_r.touchdraw.ui.textMode.TextModeViewModel
import com.abizer_r.touchdraw.utils.drawMode.CustomLayerTypeComposable
import com.abizer_r.touchdraw.utils.drawMode.getOpacityOrNull
import com.abizer_r.touchdraw.utils.drawMode.getShapeTypeOrNull
import com.abizer_r.touchdraw.utils.drawMode.getWidthOrNull
import com.abizer_r.touchdraw.utils.editorScreen.EditorScreenUtils
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Stack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen(
    bitmap: Bitmap,
    editorScreenState: EditorScreenState,
    bottomToolbarState: BottomToolbarState = EditorScreenUtils.getDefaultBottomToolbarState(),
    enableUndo: Boolean = false,
    enableRedo: Boolean = false,
    onUndo: () -> Unit = {},
    onRedo: () -> Unit = {},
    goToDrawModeScreen: (bitmap: Bitmap) -> Unit,
    goToTextModeScreen: (bitmap: Bitmap) -> Unit,
) {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.background

    val onBottomToolbarItemClicked = remember<(BottomToolbarEvent) -> Unit> {{ toolbarEvent ->
        if (toolbarEvent is BottomToolbarEvent.OnItemClicked) {
            when (toolbarEvent.toolbarItem) {
                BottomToolbarItem.DrawMode -> {
                    goToDrawModeScreen(bitmap)
                }
                BottomToolbarItem.TextMode -> {
                    goToTextModeScreen(bitmap)
                }
                else -> {}
            }
        }
    }}

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val (topToolbar, bottomToolbar, bgImage) = createRefs()

        TopToolBar(
            modifier = Modifier.constrainAs(topToolbar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            enableUndo = enableUndo,
            enableRedo = enableRedo,
            showCloseAndDone = false,
            onUndo = onUndo,
            onRedo = onRedo
        )

        val aspectRatio = bitmap?.let {
            bitmap.width.toFloat() / bitmap.height.toFloat()
        }
        val screenShotBoxWidth = if (aspectRatio != null) {
            Dimension.ratio(aspectRatio.toString())
        } else Dimension.fillToConstraints
        Box(
            modifier = Modifier.constrainAs(bgImage) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(topToolbar.bottom)
                bottom.linkTo(bottomToolbar.top)
                width = screenShotBoxWidth
                height = Dimension.fillToConstraints
            }
        ) {

            Image(
                modifier = Modifier
                    .fillMaxSize(),
                bitmap = bitmap.asImageBitmap(),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                alpha = 1f
            )
        }


        BottomToolBar(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            bottomToolbarState = bottomToolbarState,
            onEvent = onBottomToolbarItemClicked
        )


    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    SketchDraftTheme {
        EditorScreen(
            bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_2).asAndroidBitmap(),
            editorScreenState = EditorScreenState(),
//            bottomToolbarState = EditorScreenUtils.getDefaultBottomToolbarState(),
            goToTextModeScreen = {},
            goToDrawModeScreen = {}
        )
    }
}