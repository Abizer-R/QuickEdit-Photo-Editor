package com.abizer_r.touchdraw.ui.effectsMode.effectsPreview

import android.graphics.Bitmap

data class EffectItem(
//    val id: String,
    val ogBitmap: Bitmap,
    val previewBitmap: Bitmap,
    val label: String
)