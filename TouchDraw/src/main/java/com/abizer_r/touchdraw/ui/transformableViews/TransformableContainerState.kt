package com.abizer_r.touchdraw.ui.transformableViews

data class TransformableContainerState(
    val childrenStateList: ArrayList<TransformableBoxState> = arrayListOf(),
    val triggerRecomposition: Long = 0
)
