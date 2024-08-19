package com.abizer_r.quickedit.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.utils.defaultTextColor
import com.abizer_r.quickedit.utils.PermissionUtils
import com.abizer_r.quickedit.utils.PermissionUtils.PermissionTypes

@Composable
fun PermissionDeniedView(
    modifier: Modifier,
    permissionTypes: ArrayList<PermissionTypes>,
    launchAppSettings: () -> Unit
) {
    val context = LocalContext.current
    val permissionText = PermissionUtils.getTextForPermissionsSettingsDialog(
        context, permissionTypes
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier,
            text = permissionText,
            style = MaterialTheme.typography.titleMedium.copy(
                color = defaultTextColor()
            )
        )

        Button(
            onClick = { launchAppSettings() }
        ) {
            Text(text = stringResource(id = R.string.grant_permission))
        }
    }

}