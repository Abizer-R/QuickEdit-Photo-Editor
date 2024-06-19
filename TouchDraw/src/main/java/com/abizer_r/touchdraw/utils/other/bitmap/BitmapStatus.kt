package com.abizer_r.touchdraw.utils.other.bitmap

import android.graphics.Bitmap

sealed class BitmapStatus {
    object None: BitmapStatus()
    object Processing: BitmapStatus()
    data class Success(val scaledBitmap: Bitmap): BitmapStatus()
    data class Failed(val exception: Exception? = null, val errorMsg: String? = null): BitmapStatus()
}