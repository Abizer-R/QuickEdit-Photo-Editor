package com.abizer_r.quickedit.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abizer_r.quickedit.R
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
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        )

        Button(
            onClick = { launchAppSettings() }
        ) {
            Text(text = stringResource(id = R.string.grant_permission))
        }
    }

}