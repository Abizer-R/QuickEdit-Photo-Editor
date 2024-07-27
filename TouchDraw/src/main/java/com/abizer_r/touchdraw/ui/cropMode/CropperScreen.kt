package com.abizer_r.touchdraw.ui.cropMode

import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.touchdraw.utils.other.bitmap.ImmutableBitmap
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
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .background(Color.Yellow)
        ) {
            AndroidView(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White)
                    .padding(8.dp),
                factory = { context ->
                    Log.e("TEST_crop", "CropperScreen: factory", )
                    CropImageView(context).apply {
                        setImageBitmap(immutableBitmap.bitmap)
                        setImageCropOptions(CropImageOptions())
                        setOnCropImageCompleteListener(cropCompleteListener)
                    }
                },
                update = { cropImageView ->
                    Log.e("TEST_crop", "CropperScreen: update", )
                    cropImageLambda(cropImageView)
                }
            )
        }

//        Button(
//            modifier = Modifier
//                .constrainAs(bottomToolbar) {
//                    bottom.linkTo(parent.bottom)
//                    width = Dimension.matchParent
//                    height = Dimension.wrapContent
//                },
//            onClick = {
//                shouldCrop = true
//            }
//        ) {
//            Text("crop")
//        }

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    SketchDraftTheme {
        CropperScreen(
            immutableBitmap = ImmutableBitmap(
                ImageBitmap.imageResource(id = R.drawable.placeholder_image_2).asAndroidBitmap()
            ),
            onDoneClicked = {},
            onBackPressed = {}
        )
    }
}