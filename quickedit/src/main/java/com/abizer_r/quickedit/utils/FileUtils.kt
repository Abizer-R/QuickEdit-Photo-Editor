package com.abizer_r.quickedit.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream

object FileUtils {

    fun getUriForFile(context: Context, file: File): Uri? {
        return FileProvider.getUriForFile(context, "com.abizer_r.quickedit.fileprovider", file)
    }

    fun saveFileToAppFolder(
        context: Context,
        file: File,
        outputFileName: String? = null,
        onSuccess: () -> Unit,
        onFailure: (errorMsg: String?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveFileToAppFolderAndroid(context, file, outputFileName, onSuccess, onFailure)
        } else {
            saveFileToAppFolderAndroidLegacy(context, file, outputFileName, onSuccess, onFailure)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveFileToAppFolderAndroid(
        context: Context,
        file: File,
        outputFileName: String? = null,
        onSuccess: () -> Unit,
        onFailure: (errorMsg: String?) -> Unit
    ) {

        val filename = if (outputFileName.isNullOrBlank()) {
            System.currentTimeMillis().toString()
        } else outputFileName
        val mimeType = getMimeTypeFromFile(file)
        val relativePath = getRelativePathFromMimeTypeNew(context, mimeType)


        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename + "." + file.extension)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }

        val contentUri: Uri = when {
            mimeType.startsWith("image/") -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            mimeType.startsWith("video/") -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }

        val uri = context.contentResolver.insert(contentUri, contentValues)
        if (uri != null) {
            copyFileToUri(context.contentResolver, file, uri)
            onSuccess()
        } else {
            onFailure(null)
        }
    }

    private fun saveFileToAppFolderAndroidLegacy(
        context: Context,
        file: File,
        outputFileName: String? = null,
        onSuccess: () -> Unit,
        onFailure: (errorMsg: String?) -> Unit
    ) {
        val filename = if (outputFileName.isNullOrBlank()) {
            System.currentTimeMillis().toString()
        } else outputFileName
        val mimeType = getMimeTypeFromFile(file)
        val relativePath = getRelativePathFromMimeTypeNew(context, mimeType)
        val externalStorageDir = File(getExternalStorageRootLegacy(), "$relativePath/$filename" + "." + file.extension)

        externalStorageDir.parentFile?.mkdirs()

        try {
            copyFile(file, externalStorageDir)
        } catch (e: Exception) {
            onFailure(e.message)
        }

        // Make the file visible in the gallery
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DATA, externalStorageDir.absolutePath)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        }
        val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)


        if (uri != null) {
            copyFileToUri(context.contentResolver, file, uri)
            onSuccess()
        } else {
            onFailure(null)
        }
    }

    private fun getExternalStorageRootLegacy(): File {
        return Environment.getExternalStorageDirectory()
    }

    private fun getMimeTypeFromFile(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg", "png" -> "image/${file.extension.lowercase()}"
            "mp4" -> "video/mp4"
            else -> "application/octet-stream"
        }
    }

    private fun getRelativePathFromMimeTypeNew(context: Context, mimeType: String): String {
        return when {
            mimeType.startsWith("image/") -> Environment.DIRECTORY_PICTURES + "/QuickEdit Images"
            mimeType.startsWith("video/") -> Environment.DIRECTORY_MOVIES + "/QuickEdit Videos"
            else -> Environment.DIRECTORY_DOCUMENTS + "/QuickEdit Documents"
        }
    }

    private fun getRelativePathFromMimeType(context: Context, mimeType: String): String {
        return when {
            mimeType.startsWith("image/") -> getQuickEditImagesFolderPath(context)
            mimeType.startsWith("video/") -> getQuickEditVideosFolderPath(context)
            else -> getQuickEditDocumentsFolderPath(context)
        }
    }

    fun copyFileToUri(resolver: ContentResolver, file: File, uri: Uri) {
        resolver.openOutputStream(uri)?.use { outputStream ->
            FileInputStream(file).use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    fun copyFile(sourceFile: File, destFile: File) {
        FileInputStream(sourceFile).use { inputStream ->
            destFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    fun getQuickEditImagesFolderPath(context: Context): String {
        return getAppMediaParentFolder(context).plus("/QuickEdit Images")
    }

    fun getQuickEditVideosFolderPath(context: Context): String {
        return getAppMediaParentFolder(context).plus("/QuickEdit Videos")
    }

    fun getQuickEditAudioFolderPath(context: Context): String {
        return getAppMediaParentFolder(context).plus("/QuickEdit Audios")
    }

    fun getQuickEditDocumentsFolderPath(context: Context): String {
        return getAppMediaParentFolder(context).plus("/QuickEdit Documents")
    }

    fun getAppMediaParentFolder(context: Context): String {
        return getAndroidMediaDirectoryPath(context).plus("/Media")
    }

    fun getAndroidMediaDirectoryPath(context: Context): String {
        return "Android/media/${context.packageName}/QuickEdit"
    }
}