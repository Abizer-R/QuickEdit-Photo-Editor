package com.abizer_r.touchdraw.ui.transformableViews.containerView

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.abizer_r.touchdraw.ui.transformableViews.childView.TransformableBoxState

data class TransformableContainerState(
    val childrenStateList: ArrayList<TransformableBoxState> = arrayListOf(),
    val triggerRecomposition: Long = 0
)
