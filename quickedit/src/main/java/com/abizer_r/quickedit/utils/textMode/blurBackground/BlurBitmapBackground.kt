package com.abizer_r.quickedit.utils.textMode.blurBackground

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.skydoves.cloudy.cloudy

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BlurBitmapBackground(
    modifier: Modifier,
    imageBitmap: ImageBitmap,
    contentScale: ContentScale,
    shouldBlur: Boolean,
    blurRadius: Int = 15,
    onBgClicked: () -> Unit
) {
    /**
     * WORK AROUND - depending on case (textField visible or not), adding/removing a "Cloudy" composable
     * REASON - Seems like the "Cloudy" composable is converting the view to bitmap before blurring (or something like that)
     *          So, when the textField is not visible, there is a permanent image of the newly created textBox with the selection layout.
     *          This creates a bug, and I'm not able to figure out how to prevent this
     */
    if (shouldBlur) {
        Log.e("TEST_BLUR", "BlurBitmapBackground: ", )
        Image(
            modifier = modifier.fillMaxSize().cloudy(
                radius = blurRadius
            ),
            bitmap = imageBitmap,
            contentScale = contentScale,
            contentDescription = null,
            alpha = 0.5f
        )
    } else {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        onBgClicked()
                    }
                },
            bitmap = imageBitmap,
            contentScale = contentScale,
            contentDescription = null,
            alpha = 1f
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBitmapBg() {
    QuickEditTheme {

        BlurBitmapBackground(
            modifier = Modifier.fillMaxSize(),
            imageBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_3),
            shouldBlur = false,
            contentScale = ContentScale.Fit,
            onBgClicked = {}
        )
    }
}