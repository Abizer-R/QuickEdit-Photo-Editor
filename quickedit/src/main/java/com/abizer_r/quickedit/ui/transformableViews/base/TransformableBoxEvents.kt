package com.abizer_r.quickedit.ui.transformableViews.base

import androidx.compose.ui.geometry.Offset

sealed class TransformableBoxEvents {
    abstract val id: String

    data class OnDrag(override val id: String, val dragAmount: Offset): TransformableBoxEvents()
    data class OnZoom(override val id: String, val zoomAmount: Float): TransformableBoxEvents()
    data class OnRotate(override val id: String, val rotationChange: Float): TransformableBoxEvents()
    data class OnCloseClicked(override val id: String): TransformableBoxEvents()
    data class OnTapped(override val id: String, val textViewState: TransformableTextBoxState?): TransformableBoxEvents()
}

//    data class OnTransform(val id: String, val newOffSet: Offset, val newScale: Float, val newRotation: Float): TransformableBoxEvents()
