package com.abizer_r.quickedit.ui.common.permission

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.R

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkayClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.permission_required))
        },
        text = {
            Text(text = permissionTextProvider.getDescription(
                context, isPermanentlyDeclined
            ))
        },
        confirmButton = {
            PermissionDialogConfirmButton(
                isPermanentlyDeclined = isPermanentlyDeclined,
                onGoToAppSettingsClick = onGoToAppSettingsClick,
                onOkayClick = onOkayClick
            )
        }
    )
}

@Composable
private fun PermissionDialogConfirmButton(
    isPermanentlyDeclined: Boolean,
    onGoToAppSettingsClick: () -> Unit,
    onOkayClick: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        val btnText = stringResource(id = if (isPermanentlyDeclined) {
            R.string.grant_permission
        } else R.string.okay)
        Text(
            text = btnText,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isPermanentlyDeclined) {
                        onGoToAppSettingsClick()
                    } else {
                        onOkayClick()
                    }
                }

        )
    }
}

interface PermissionTextProvider {
    fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String
}

class StoragePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        return context.getString(
            if (isPermanentlyDeclined) {
                R.string.storage_permission_permanently_declined
            } else R.string.storage_permission_rationale
        )
    }
}