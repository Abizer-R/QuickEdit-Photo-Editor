package com.abizer_r.quickedit.ui.cropMode

import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.utils.defaultErrorToast
import com.abizer_r.quickedit.ui.common.AnimatedToolbarContainer
import com.abizer_r.quickedit.ui.common.bottomToolbarModifier
import com.abizer_r.quickedit.ui.common.crop.AspectRatioDialog
import com.abizer_r.quickedit.ui.common.topToolbarModifier
import com.abizer_r.quickedit.ui.cropMode.cropperOptions.CropperOption
import com.abizer_r.quickedit.ui.cropMode.cropperOptions.CropperOptionsFullWidth
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_LARGE
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_SMALL
import com.abizer_r.quickedit.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.quickedit.utils.editorScreen.CropModeUtils
import com.abizer_r.quickedit.utils.getActivity
import com.abizer_r.quickedit.utils.other.anim.AnimUtils
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap
import com.abizer_r.quickedit.utils.toast
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.OnCropImageCompleteListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CropperScreen(
    immutableBitmap: ImmutableBitmap,
    onDoneClicked: (bitmap: Bitmap) -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val activity = LocalContext.current.getActivity()
    val lifeCycleOwner = LocalLifecycleOwner.current

    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.background

    val topToolbarHeight =  TOOLBAR_HEIGHT_SMALL
    val bottomToolbarHeight = TOOLBAR_HEIGHT_LARGE

    var toolbarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        toolbarVisible = true
    }

    // Save the previous state
    val previousSoftInputMode = remember {
        activity?.window?.attributes?.softInputMode ?: WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED
    }

    // Set the desired windowSoftInputMode
    DisposableEffect(Unit) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        onDispose {
            // Restore the previous windowSoftInputMode
            activity?.window?.setSoftInputMode(previousSoftInputMode)
        }
    }

    var shouldCrop by remember { mutableStateOf(false) }
    var showCropRatioDialog by remember { mutableStateOf(false) }
    val cropperOptionsList = remember { CropModeUtils.getCropperOptionsList() }
    var selectedCropOption by remember { mutableIntStateOf(0) }
    var cropImageOptions by remember {
        mutableStateOf(CropImageOptions())
    }

    val onCloseClickedLambda = remember<() -> Unit> { {
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            toolbarVisible = false
            delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
            onBackPressed()
        }
    }}

    val onDoneClickedLambda = remember<() -> Unit> { {
        shouldCrop = true
    }}

    BackHandler {
        onCloseClickedLambda()
    }


    val handleCropResult = remember<(Bitmap) -> Unit> {{ bitmap ->
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            toolbarVisible = false
            delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
            onDoneClicked(bitmap)
        }
    }}

    val cropCompleteListener = remember {
        OnCropImageCompleteListener { view, result ->
            result.bitmap?.let {
                handleCropResult(it)
            } ?: context.defaultErrorToast()
        }
    }

    val onCropOptionItemClicked = remember<(Int, CropperOption) -> Unit> {{ position, cropOption ->
        selectedCropOption = position
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
    }}

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val (topToolBar, cropView, bottomToolbar) = createRefs()

        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = topToolbarModifier(topToolBar)
        ) {
            TextModeTopToolbar(
                modifier = Modifier,
                toolbarHeight = topToolbarHeight,
                onCloseClicked = onCloseClickedLambda,
                onDoneClicked = onDoneClickedLambda
            )
        }


        Box(
            modifier = Modifier
                .constrainAs(cropView) {
                    width = Dimension.matchParent
                    height = Dimension.matchParent
                }
                .padding(top = topToolbarHeight, bottom = bottomToolbarHeight)
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { context ->
                    CropImageView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setImageBitmap(immutableBitmap.bitmap)
                        setImageCropOptions(cropImageOptions)
                        setOnCropImageCompleteListener(cropCompleteListener)
                    }
                },
                update = { cropImageView ->
                    if (shouldCrop) {
                        cropImageView.croppedImageAsync()
                    }
                    cropImageView.setImageCropOptions(cropImageOptions)
                }
            )
        }


        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = bottomToolbarModifier(bottomToolbar),
        ) {
            CropperOptionsFullWidth(
                modifier = Modifier,
                toolbarHeight = bottomToolbarHeight,
                cropperOptionList = cropperOptionsList,
                selectedIndex = selectedCropOption,
                onItemClicked = onCropOptionItemClicked
            )
        }

        AnimatedVisibility(
            visible = showCropRatioDialog,
        ) {
            AspectRatioDialog(
                onDismiss = { showCropRatioDialog = false },
                onSetRatio = { x, y ->
                    context.toast("x = $x, y = $x. r = ${x.toFloat() / y.toFloat()}")
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    QuickEditTheme {
        CropperScreen(
            immutableBitmap = ImmutableBitmap(
                ImageBitmap.imageResource(id = R.drawable.placeholder_image_3).asAndroidBitmap()
            ),
            onDoneClicked = {},
            onBackPressed = {}
        )
    }
}