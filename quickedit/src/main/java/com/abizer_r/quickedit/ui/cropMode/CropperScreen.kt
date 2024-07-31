package com.abizer_r.quickedit.ui.cropMode

import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.R
import com.abizer_r.components.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.cropMode.cropperOptions.CropperOptionsFullWidth
import com.abizer_r.quickedit.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.quickedit.utils.editorScreen.CropModeUtils
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.OnCropImageCompleteListener

@Composable
fun CropperScreen(
    immutableBitmap: ImmutableBitmap,
    onDoneClicked: (bitmap: Bitmap) -> Unit,
    onBackPressed: () -> Unit
) {

    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.background

    var shouldCrop by remember {
        mutableStateOf(false)
    }

    val cropperOptionsList = remember {
        CropModeUtils.getCropperOptionsList()
    }

    var selectedCropOption by remember {
        mutableIntStateOf(0)
    }

    var cropImageOptions = remember {
        CropImageOptions()
    }

    val cropImageLambda = remember<(CropImageView) -> Unit> {{ cropImageView ->
        if (shouldCrop) {
            cropImageView.croppedImageAsync()
        }
    }}

    val onCloseClickedLambda = remember<() -> Unit> { {
        onBackPressed()
    }}

    val onDoneClickedLambda = remember<() -> Unit> { {
        shouldCrop = true
    }}



    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val (topToolBar, cropView, bottomToolbar) = createRefs()

        val cropCompleteListener = remember {
            OnCropImageCompleteListener { view, result ->
                result.bitmap?.let {
                    onDoneClicked(it)
                }
            }
        }

        TextModeTopToolbar(
            modifier = Modifier.constrainAs(topToolBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            onCloseClicked = onCloseClickedLambda,
            onDoneClicked = onDoneClickedLambda
        )


        Box(
            modifier = Modifier
                .constrainAs(cropView) {
                    top.linkTo(topToolBar.bottom)
                    bottom.linkTo(bottomToolbar.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { context ->
                    Log.e("TEST_crop", "CropperScreen: factory", )
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
                    Log.e("TEST_crop", "CropperScreen: update", )
                    cropImageLambda(cropImageView)
                    cropImageView.setImageCropOptions(cropImageOptions)
                }
            )
        }

        CropperOptionsFullWidth(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            cropperOptionList = cropperOptionsList,
            selectedIndex = selectedCropOption,
            onItemClicked = { position, cropOption ->
                selectedCropOption = position
                when (cropOption.aspectRatioX) {
                    -1f -> {
                        cropImageOptions = cropImageOptions.copy(
                            fixAspectRatio = false,
                            aspectRatioX = 1,
                            aspectRatioY = 1
                        )
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

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    QuickEditTheme {
        CropperScreen(
            immutableBitmap = ImmutableBitmap(
                ImageBitmap.imageResource(id = R.drawable.placeholder_image_1).asAndroidBitmap()
            ),
            onDoneClicked = {},
            onBackPressed = {}
        )
    }
}