package io.github.abizerr.quickedit.tool.crop

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.OnCropImageCompleteListener
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.tool.crop.model.CropperOption
import io.github.abizerr.quickedit.tool.crop.utils.CropModeUtils
import io.github.abizerr.quickedit.ui.utils.PreviewUtils
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolController
import io.github.abizerr.quickedit.ui.common.AnimatedToolbarContainer
import io.github.abizerr.quickedit.ui.common.TOOLBAR_HEIGHT_LARGE
import io.github.abizerr.quickedit.ui.common.TOOLBAR_HEIGHT_SMALL
import io.github.abizerr.quickedit.ui.theme.QuickEditTheme
import io.github.abizerr.quickedit.ui.theme.ToolBarBackgroundColor
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST
import io.github.abizerr.quickedit.ui.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val topToolbarHeight = TOOLBAR_HEIGHT_SMALL
private val bottomToolbarHeight = TOOLBAR_HEIGHT_LARGE

@Composable
fun CropToolScreen(
    state: QuickEditState,
    controller: ToolController,
    onExit: () -> Unit
) {
    val context = LocalContext.current

    var toolbarVisible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    var cropView: CropImageView? by remember { mutableStateOf(null) }
    var cropImageOptions by remember { mutableStateOf(CropImageOptions()) }

    val cropperOptionsList = remember { CropModeUtils.getCropperOptionsList() }
    var selectedCropOptionIndex by remember { mutableIntStateOf(0) }
    var showCropRatioDialog by remember { mutableStateOf(false) }


    fun handleCropResult(croppedBitmap: Bitmap) {
        scope.launch {
            controller.emit(EditOp.ImageCropped(croppedBitmap))
            toolbarVisible = false
            delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
            onExit()
        }
    }

    val cropCompleteListener = remember {
        OnCropImageCompleteListener { view, result ->
            result.bitmap?.let {
                handleCropResult(it)
            } // TODO Show Error toast using "?:"
        }
    }

    Box(Modifier.fillMaxSize()) {
        TopToolbar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            visible = toolbarVisible,
            height = topToolbarHeight,
            onClose = {
                scope.launch {
                    toolbarVisible = false
                    delay(TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
                    onExit()
                }
            },
            onDone = {
                cropView?.croppedImageAsync()
            }
        )

        BottomToolbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            visible = toolbarVisible,
            height = bottomToolbarHeight,
            cropperOptionsList = cropperOptionsList,
            selectedCropOptionIndex = selectedCropOptionIndex,
            onCropOptionItemClicked = { position, cropOption ->
                selectedCropOptionIndex = position
                when (cropOption.aspectRatioX) {
                    -1f -> {
                        cropImageOptions = cropImageOptions.copy(
                            fixAspectRatio = false,
                            aspectRatioX = 1,
                            aspectRatioY = 1
                        )
                    }
                    -2f -> {
                        showCropRatioDialog = true
                    }
                    else -> {
                        cropImageOptions = cropImageOptions.copy(
                            fixAspectRatio = true,
                            aspectRatioX = cropOption.aspectRatioX.toInt(),
                            aspectRatioY = cropOption.aspectRatioY.toInt()
                        )
                    }
                }
            }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(top = topToolbarHeight, bottom = bottomToolbarHeight)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->

                    // 1) Stable container the compose world will measure/layout
                    val container = android.widget.FrameLayout(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        clipToPadding = false
                    }

                    // 2) The self-mutating library with its own onLayout()
                    // which triggers compose's measure causing recurring calls and ANR
                    // Adding this inside the stable container fixes the recurring calls and ANR
                    val mCropView = CropImageView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        when (val img = state.snapshot.image) {
                            is EditImage.FromBitmap -> setImageBitmap(img.bitmap)
                            is EditImage.FromUri -> setImageUriAsync(img.uri)
                        }
                        setImageCropOptions(cropImageOptions)
                        setOnCropImageCompleteListener(cropCompleteListener)
                    }

                    container.addView(mCropView)
                    cropView = mCropView
                    container
                },
                update = { container ->
                    val mCropView = cropView ?: return@AndroidView
                    mCropView.setImageCropOptions(cropImageOptions)
                }
            )
        }

        AnimatedVisibility(
            visible = showCropRatioDialog,
        ) {
            AspectRatioDialog(
                onDismiss = { showCropRatioDialog = false },
                onSetRatio = { x, y ->
                    context.toast("x = $x, y = $x. r = ${x.toFloat() / y.toFloat()}")
                    selectedCropOptionIndex = cropperOptionsList.indexOfFirst { it.aspectRatioX == -2f }
                    cropImageOptions = cropImageOptions.copy(
                        fixAspectRatio = true,
                        aspectRatioX = x,
                        aspectRatioY = y
                    )
                    showCropRatioDialog = false
                }
            )
        }
    }

}

@Composable
private fun TopToolbar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    height: Dp,
    onClose: () -> Unit,
    onDone: () -> Unit
) {
    /**
     *
     *
     *
     * TODO - 1: Replace hardcoded strings with string resources
     * TODO - 2: Continue with modularization plan
     *
     *
     *
     *
     *
     *
     */
    AnimatedToolbarContainer(
        toolbarVisible = visible,
        modifier = modifier
    ) {
        Surface(tonalElevation = 2.dp) {
            Row(
                Modifier
                    .height(height)
                    .fillMaxSize()
                    .background(ToolBarBackgroundColor)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onClose() },
                    enabled = true
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                    )
                }
                Text(
                    text = "Crop",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = { onDone() },
                    enabled = true
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomToolbar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    height: Dp,
    cropperOptionsList: List<CropperOption>,
    selectedCropOptionIndex: Int,
    onCropOptionItemClicked: (pos: Int, option: CropperOption) -> Unit,
) {
    AnimatedToolbarContainer(
        toolbarVisible = visible,
        modifier = modifier
    ) {
        Surface(tonalElevation = 3.dp) {
            CropperOptionsFullWidth(
                modifier = Modifier,
                toolbarHeight = height,
                cropperOptionList = cropperOptionsList,
                selectedIndex = selectedCropOptionIndex,
                onItemClicked = onCropOptionItemClicked
            )
        }
    }
}


@Preview @Composable
private fun PreviewTopToolbar() {
    QuickEditTheme {
        TopToolbar(
            visible = true,
            height = topToolbarHeight,
            onClose = {},
            onDone = {}
        )
    }
}

@Preview @Composable
private fun PreviewBottomToolbar() {
    QuickEditTheme {
        BottomToolbar(
            visible = true,
            height = bottomToolbarHeight,
            cropperOptionsList = CropModeUtils.getCropperOptionsList(),
            selectedCropOptionIndex = 0,
            onCropOptionItemClicked = {_, _ -> }
        )
    }
}

@Preview @Composable
private fun PreviewCropToolScreen() {
    QuickEditTheme {
        CropToolScreen(
            state = PreviewUtils.getDummyEditorState(),
            controller = PreviewUtils.getDummyToolController(),
            onExit = {}
        )
    }
}
