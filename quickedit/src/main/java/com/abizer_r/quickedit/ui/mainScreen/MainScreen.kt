package com.abizer_r.quickedit.ui.mainScreen

import android.Manifest
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.common.ErrorView
import com.abizer_r.quickedit.ui.common.LoadingView
import com.abizer_r.quickedit.ui.common.PermissionDeniedView
import com.abizer_r.quickedit.ui.common.permission.PermissionDialog
import com.abizer_r.quickedit.ui.common.permission.StoragePermissionTextProvider
import com.abizer_r.quickedit.ui.navigation.NavDestinations
import com.abizer_r.quickedit.utils.FileUtils
import com.abizer_r.quickedit.utils.PermissionUtils
import com.abizer_r.quickedit.utils.PermissionUtils.PermissionTypes
import com.abizer_r.quickedit.utils.getActivity
import com.abizer_r.quickedit.utils.getOpenAppSettingsIntent
import com.abizer_r.quickedit.utils.other.bitmap.BitmapStatus
import com.abizer_r.quickedit.utils.other.bitmap.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MainScreen(
    onImageSelected: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val activity = context.getActivity()
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifeCycleOwner.lifecycleScope

    val viewModel = hiltViewModel<MainScreenViewModel>()
    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val permissionsGranted = viewModel.permissionsGranted.value

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { perms ->
        perms.keys.forEach { permission ->
            viewModel.onPermissionResult(
                permission = permission,
                isGranted = perms[permission] == true
            )
        }
        if (perms.values.all { true }) {
            viewModel.permissionsGranted.value = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        storagePermissionLauncher.launch(PermissionUtils.getInternalStoragePermissions())
    }

    val appSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        storagePermissionLauncher.launch(PermissionUtils.getInternalStoragePermissions())
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var scaledBitmapStatus by remember { mutableStateOf<BitmapStatus>(BitmapStatus.None) }

    val cameraImageUri = remember {
        val imgFile = File(context.filesDir, "camera_photo.jpg")
        FileUtils.getUriForFile(context, imgFile)
    }

    val onPhotoPicked = remember<(Uri?) -> Unit> {{
        imageUri = it
    }}

    val onPhotoCaptured = remember<(Uri?) -> Unit> {{
        imageUri = it
    }}

    LaunchedEffect(key1 = imageUri) {
        imageUri?.let { imgUri ->
            lifecycleScope.launch(Dispatchers.IO) {
                BitmapUtils.getScaledBitmap(context, imgUri).onEach {
                    scaledBitmapStatus = it
                }.collect()
            }
        }
    }

    val handleImageSelected = remember<(Bitmap) -> Unit> {{
        lifecycleScope.launch {
//            delay(1000)
            onImageSelected(it)
        }
    }}

    when (val bitmapStatus = scaledBitmapStatus) {
        BitmapStatus.None -> {
            if (cameraImageUri != null && permissionsGranted) {
                MainScreenLayout(
                    cameraImageUri,
                    onPhotoPicked,
                    onPhotoCaptured
                )
            } else if (permissionsGranted.not()){
                PermissionDeniedView(
                    modifier = Modifier.fillMaxSize().padding(48.dp),
                    permissionTypes = arrayListOf(PermissionTypes.INTERNAL_STORAGE),
                    launchAppSettings = {
                        appSettingsLauncher.launch(context.getOpenAppSettingsIntent())
                    }
                )
            } else {
                ErrorView(modifier = Modifier.fillMaxSize())
            }
        }

        BitmapStatus.Processing -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }

        is BitmapStatus.Failed -> {
            val errorText = bitmapStatus.errorMsg ?: bitmapStatus.exception?.message
            ?: stringResource(R.string.something_went_wrong)
            ErrorView(
                modifier = Modifier.fillMaxSize(),
                errorText = errorText
            )
        }

        is BitmapStatus.Success -> {
            handleImageSelected(bitmapStatus.scaledBitmap)
            // show layout to avoid showing blank screen while transition animation is played
            MainScreenLayout(
                cameraImageUri!!,
                onPhotoPicked,
                onPhotoCaptured
            )
        }
    }

    dialogQueue.reversed().forEach { permission ->
        PermissionDialog(
            permissionTextProvider = when (permission) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    StoragePermissionTextProvider()
                }
                else -> return@forEach
            },
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                activity ?: return@forEach,
                permission
            ),
            onDismiss = viewModel::dismissDialog,
            onOkayClick = {
                viewModel.dismissDialog()
                storagePermissionLauncher.launch(
                    arrayOf(permission)
                )
            },
            onGoToAppSettingsClick = {
                appSettingsLauncher.launch(
                    context.getOpenAppSettingsIntent()
                )
            }
        )
    }
}

@Composable
fun MainScreenLayout(
    cameraImageUri: Uri,
    onPhotoPicked: (Uri?) -> Unit,
    onPhotoCaptured: (Uri?) -> Unit,

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
        modifier = Modifier.fillMaxSize(),
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
            photoCaptureLauncher.launch(cameraImageUri)
        }
        ) { Text(stringResource(R.string.capture_image)) }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainScreen() {
    QuickEditTheme {
        MainScreenLayout(
            cameraImageUri = Uri.EMPTY,
            onPhotoPicked = {},
            onPhotoCaptured = {}
        )
    }
}