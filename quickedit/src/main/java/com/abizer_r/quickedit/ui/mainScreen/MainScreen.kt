package com.abizer_r.quickedit.ui.mainScreen

import android.Manifest
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.common.permission.PermissionDialog
import com.abizer_r.quickedit.ui.common.permission.StoragePermissionTextProvider
import com.abizer_r.quickedit.utils.FileUtils
import com.abizer_r.quickedit.utils.PermissionUtils
import com.abizer_r.quickedit.utils.getActivity
import com.abizer_r.quickedit.utils.getOpenAppSettingsIntent
import com.abizer_r.quickedit.utils.other.bitmap.BitmapStatus
import com.abizer_r.quickedit.utils.other.bitmap.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
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

    val onPhotoPicked = remember<(Uri?) -> Unit> {
        {
            imageUri = it
        }
    }

    val onPhotoCaptured = remember<(Uri?) -> Unit> {
        {
            imageUri = it
        }
    }

    LaunchedEffect(key1 = imageUri) {
        imageUri?.let { imgUri ->
            lifecycleScope.launch(Dispatchers.IO) {
                BitmapUtils.getScaledBitmap(context, imgUri).onEach {
                    scaledBitmapStatus = it
                }.collect()
            }
        }
    }

    MainScreenLayout(
        modifier = modifier,
        scaledBitmapStatus = scaledBitmapStatus,
        permissionsGranted = permissionsGranted,
        cameraImageUri = cameraImageUri,
        appSettingsLauncher = appSettingsLauncher,
        onPhotoPicked = onPhotoPicked,
        onPhotoCaptured = onPhotoCaptured,
        onImageSelected = onImageSelected
    )

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