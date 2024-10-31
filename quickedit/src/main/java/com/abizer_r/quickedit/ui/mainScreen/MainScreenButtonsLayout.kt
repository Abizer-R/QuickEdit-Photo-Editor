package com.abizer_r.quickedit.ui.mainScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.ui.common.LoadingView
import com.abizer_r.quickedit.utils.toast

@Composable
fun MainScreenButtonsLayout(
    modifier: Modifier = Modifier.fillMaxWidth(),
    cameraImageUri: Uri?,
    onPhotoPicked: (Uri?) -> Unit,
    onPhotoCaptured: (Uri?) -> Unit,
    showLoading: Boolean = false
) {
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onPhotoPicked
    )

    val photoCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isTaken ->
            if (isTaken) onPhotoCaptured(cameraImageUri)
        }
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        ) { Text(stringResource(R.string.pick_image)) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (cameraImageUri != null) {
                photoCaptureLauncher.launch(cameraImageUri)
            } else {
                context.toast(R.string.something_went_wrong)
            }
        }
        ) { Text(stringResource(R.string.capture_image)) }

        Spacer(modifier = Modifier.height(16.dp))

        LoadingView(
            modifier = Modifier
                .size(48.dp)
                .alpha(if (showLoading) 1f else 0f),
            progressBarSize = 32.dp,
            progressBarStrokeWidth = 3.dp
        )
    }
}