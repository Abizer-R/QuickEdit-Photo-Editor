package com.abizer_r.quickedit.ui.effectsMode

import android.graphics.Bitmap
import com.abizer_r.quickedit.ui.effectsMode.effectsPreview.EffectItem

data class EffectsModeState(
    val filteredBitmap: Bitmap? = null,
    val effectsList: ArrayList<EffectItem> = arrayListOf(),
    val selectedEffectIndex: Int = 0,
//    val recompositionTrigger: Long = 0
)
