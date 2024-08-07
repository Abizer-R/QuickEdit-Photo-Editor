package com.abizer_r.quickedit.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.abizer_r.quickedit.R

object PermissionUtils {

    fun hasInternalStorageAccessPermission(context: Context?): Boolean {
        val mContext = context ?: return false
        return if (isAndroidQAndAbove()) {
            true
        } else {
            checkPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    fun getInternalStoragePermissions(): Array<String> {
        return if (isAndroidQAndAbove()) {
            arrayOf()
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun checkPermission(context: Context, permString: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permString
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isAndroidQAndAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun getTextForPermissionsSettingsDialog(
        context: Context,
        permissionTypes: ArrayList<PermissionTypes>
    ): String {
        if (permissionTypes.isEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        sb.append(context.getString(R.string.following_permissions_are_required))
        permissionTypes.forEach {
            when (it) {
                PermissionTypes.INTERNAL_STORAGE -> {
                    sb.append(context.getString(R.string.perm_item_storage))
                }
            }
        }
        return sb.toString()
    }

    enum class PermissionTypes {
        INTERNAL_STORAGE,
    }
}