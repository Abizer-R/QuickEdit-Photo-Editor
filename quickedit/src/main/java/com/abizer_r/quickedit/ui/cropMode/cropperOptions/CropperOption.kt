package com.abizer_r.quickedit.ui.cropMode.cropperOptions

import android.graphics.Bitmap
import java.util.UUID

data class CropperOption(
    val id: String = UUID.randomUUID().toString(),
    val aspectRatioX: Float,
    val aspectRatioY: Float,
    val label: String
)