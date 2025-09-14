package io.github.abizerr.quickedit.tool.crop

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.OnCropImageCompleteListener
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolContribution
import io.github.abizerr.quickedit.ui.api.ToolController
import io.github.abizerr.quickedit.ui.common.AnimatedToolbarContainer
import io.github.abizerr.quickedit.ui.common.TOOLBAR_HEIGHT_MEDIUM
import io.github.abizerr.quickedit.ui.common.TOOLBAR_HEIGHT_SMALL
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CropContribution : ToolContribution {
    override val id: String = "crop"
    override val label: String = "Crop"
    override val supportsFullScreen: Boolean get() = true

    @Composable
    override fun ToolbarIcon(selected: Boolean, onClick: () -> Unit) {
        IconWithLabel(
            selected = selected,
            imageVector = Icons.Outlined.Crop,
            labelText = label,
            onClick = onClick
        )
    }

    @Composable
    override fun Panel(state: QuickEditState, controller: ToolController) {
        Text("Crop tool placeholder")
    }

    @Composable
    override fun FullScreenTool(
        state: QuickEditState,
        controller: ToolController,
        onExit: () -> Unit
    ) {

        var toolbarVisible by remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()

        var cropView: CropImageView? by remember { mutableStateOf(null) }
        var options by remember { mutableStateOf(CropImageOptions()) }


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
            val topToolbarHeight = TOOLBAR_HEIGHT_SMALL
            val bottomToolbarHeight = TOOLBAR_HEIGHT_MEDIUM

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
                updateCropOptions = { ratioOption ->
                    options = when (ratioOption) {
                        CropRatioOptions.Free -> {
                            options.copy(fixAspectRatio = false, aspectRatioX = 1, aspectRatioY = 1)
                        }

                        CropRatioOptions.Ratio1x1 -> {
                            options.copy(fixAspectRatio = true, aspectRatioX = 1, aspectRatioY = 1)
                        }

                        CropRatioOptions.Ratio16x9 -> {
                            options.copy(fixAspectRatio = true, aspectRatioX = 16, aspectRatioY = 9)
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
                            setImageCropOptions(options)
                            setOnCropImageCompleteListener(cropCompleteListener)
                        }

                        container.addView(mCropView)
                        cropView = mCropView
                        container
                    },
                    update = { container ->
                        val mCropView = cropView ?: return@AndroidView
                        mCropView.setImageCropOptions(options)
                    }
                )
            }
        }

    }

    @Composable
    private fun TopToolbar(
        modifier: Modifier,
        visible: Boolean,
        height: Dp,
        onClose: () -> Unit,
        onDone: () -> Unit
    ) {
        AnimatedToolbarContainer(
            toolbarVisible = visible,
            modifier = modifier
        ) {
            Surface(tonalElevation = 2.dp) {
                Row(
                    Modifier
                        .height(height)
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onClose
                    ) { Text("Close") }
                    Text(
                        text = "Crop",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(
                        onClick = onDone
                    ) { Text("Done") }
                }
            }
        }
    }

    enum class CropRatioOptions {
        Free, Ratio1x1, Ratio16x9
    }

    @Composable
    private fun BottomToolbar(
        modifier: Modifier,
        visible: Boolean,
        height: Dp,
        updateCropOptions: (CropRatioOptions) -> Unit,
    ) {
        AnimatedToolbarContainer(
            toolbarVisible = visible,
            modifier = modifier
        ) {
            Surface(tonalElevation = 3.dp) {
                Row(
                    Modifier
                        .height(height)
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {
                        updateCropOptions(CropRatioOptions.Free)

                    }) { Text("Free") }
                    TextButton(onClick = {
                        updateCropOptions(CropRatioOptions.Ratio1x1)

                    }) { Text("1:1") }
                    TextButton(onClick = {
                        updateCropOptions(CropRatioOptions.Ratio16x9)
                    }) { Text("16:9") }
                }
            }
        }
    }

}
