package com.abizer_r.quickedit.ui.mainScreen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.common.AppIconWithName
import com.abizer_r.quickedit.ui.common.ErrorView
import com.abizer_r.quickedit.ui.common.PermissionDeniedView
import com.abizer_r.quickedit.utils.PermissionUtils.PermissionTypes
import com.abizer_r.quickedit.utils.getOpenAppSettingsIntent
import com.abizer_r.quickedit.utils.other.bitmap.BitmapStatus

@Composable
fun MainScreenLayout(
    modifier: Modifier = Modifier,
    scaledBitmapStatus: BitmapStatus,
    permissionsGranted: Boolean,
    cameraImageUri: Uri? = null,
    appSettingsLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>? = null,
    onPhotoPicked: (Uri?) -> Unit = {},
    onPhotoCaptured: (Uri?) -> Unit = {},
    onImageSelected: (Bitmap) -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppIconWithName(Modifier.padding(vertical = 64.dp))

        when (scaledBitmapStatus) {
            BitmapStatus.None -> MainScreenDefaultView(
                permissionsGranted,
                cameraImageUri,
                onPhotoPicked,
                onPhotoCaptured,
                appSettingsLauncher,
                context
            )

            BitmapStatus.Processing -> MainScreenButtonsLayout(
                cameraImageUri = cameraImageUri,
                onPhotoPicked = onPhotoPicked,
                onPhotoCaptured = onPhotoCaptured,
                showLoading = true
            )

            is BitmapStatus.Failed -> MainScreenErrorView(
                cameraImageUri = cameraImageUri,
                errorText = scaledBitmapStatus.errorMsg ?: scaledBitmapStatus.exception?.message,
                onPhotoPicked = onPhotoPicked,
                onPhotoCaptured = onPhotoCaptured
            )


            is BitmapStatus.Success -> {
                onImageSelected(scaledBitmapStatus.scaledBitmap)
                // show layout to avoid showing blank screen while transition animation is played
                MainScreenButtonsLayout(
                    cameraImageUri = cameraImageUri,
                    onPhotoPicked = onPhotoPicked,
                    onPhotoCaptured = onPhotoCaptured,
                )
            }
        }
    }

}

@Composable
private fun MainScreenDefaultView(
    permissionsGranted: Boolean,
    cameraImageUri: Uri?,
    onPhotoPicked: (Uri?) -> Unit,
    onPhotoCaptured: (Uri?) -> Unit,
    appSettingsLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
    context: Context
) {
    if (permissionsGranted) {
        MainScreenButtonsLayout(
            cameraImageUri = cameraImageUri,
            onPhotoPicked = onPhotoPicked,
            onPhotoCaptured = onPhotoCaptured
        )
    } else {
        PermissionDeniedView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            permissionTypes = arrayListOf(PermissionTypes.INTERNAL_STORAGE),
            launchAppSettings = {
                appSettingsLauncher?.launch(context.getOpenAppSettingsIntent())
            }
        )
    }
}

@Composable
private fun MainScreenErrorView(
    cameraImageUri: Uri?,
    errorText: String?,
    onPhotoPicked: (Uri?) -> Unit,
    onPhotoCaptured: (Uri?) -> Unit
) {
    MainScreenButtonsLayout(
        modifier = Modifier.fillMaxWidth(),
        cameraImageUri = cameraImageUri,
        onPhotoPicked = onPhotoPicked,
        onPhotoCaptured = onPhotoCaptured,
    )
    ErrorView(
        modifier = Modifier.fillMaxWidth(),
        errorText = errorText ?: stringResource(R.string.something_went_wrong)
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPermissionNotGranted() {
    QuickEditTheme {
        MainScreenLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            scaledBitmapStatus = BitmapStatus.None,
            permissionsGranted = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPermissionGranted() {
    QuickEditTheme {
        MainScreenLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            scaledBitmapStatus = BitmapStatus.None,
            permissionsGranted = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewProcessing() {
    QuickEditTheme {
        MainScreenLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            scaledBitmapStatus = BitmapStatus.Processing,
            permissionsGranted = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewFailed() {
    QuickEditTheme {
        MainScreenLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            scaledBitmapStatus = BitmapStatus.Failed(),
            permissionsGranted = true
        )
    }
}