package com.abizer_r.touchdraw.ui.effectsMode

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.abizer_r.components.util.ColorUtils
import com.abizer_r.touchdraw.ui.effectsMode.effectsPreview.EffectItem
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState

data class EffectsModeState(
    val filteredBitmap: Bitmap? = null,
    val effectsList: ArrayList<EffectItem> = arrayListOf(),
    val selectedEffectIndex: Int = 0,
//    val recompositionTrigger: Long = 0
)
