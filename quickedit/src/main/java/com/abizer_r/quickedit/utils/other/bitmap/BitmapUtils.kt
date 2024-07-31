package com.abizer_r.quickedit.utils.other.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.abizer_r.quickedit.utils.AppUtils
import kotlinx.coroutines.flow.flow


object BitmapUtils {

    suspend fun getScaledBitmap(
        context: Context,
        uri: Uri
    ) = flow<BitmapStatus> {
        emit(BitmapStatus.Processing)
        val scaledBitmap = decodeSampledBitmapFromResource(context, uri)
        if (scaledBitmap != null) {
            emit(BitmapStatus.Success(scaledBitmap))
        } else {
            emit(BitmapStatus.Failed())
        }
    }

    private fun decodeSampledBitmapFromResource(
        context: Context,
        uri: Uri,
    ): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, this)
            }

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            val screenSize = AppUtils.getScreenWidthAndHeight(context)
            val bitmapSize = getWidthAndHeightFromUri(context, uri)
            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(screenSize, bitmapSize)

            val bitmap: Bitmap? = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, this)
            }
            bitmap
        }
    }

    private fun calculateInSampleSize(
        screenSize: Pair<Int, Int>,
        bitmapSize: Pair<Int, Int>,
    ): Int {
        // Raw height and width of image
        val (reqWidth, reqHeight) = screenSize
        val (width, height) = bitmapSize

        var inSampleSize = 0

        do {
            inSampleSize++
            val compressedWidth = width / inSampleSize
            val compressedHeight = height / inSampleSize

        } while (compressedWidth > reqWidth && compressedHeight > reqHeight)

        return inSampleSize
    }

    private fun getWidthAndHeightFromUri(context: Context, uri: Uri): Pair<Int, Int> {
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, onlyBoundsOptions)
        }
        return Pair(onlyBoundsOptions.outWidth, onlyBoundsOptions.outHeight)
    }
}