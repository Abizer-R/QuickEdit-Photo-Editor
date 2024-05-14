package com.abizer_r.touchdraw.ui.transformableViews

import androidx.compose.ui.geometry.Offset
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState

sealed class TransformableViewType {
    class TextTransformable(val text: String, val viewState: TransformableBoxState) : TransformableViewType()
}

fun TransformableViewType.getId(): String {
    return when (this) {
        is TransformableViewType.TextTransformable -> {
            viewState.id
        }
    }
}

fun TransformableViewType.getPositionOffset(): Offset {
    return when (this) {
        is TransformableViewType.TextTransformable -> {
            viewState.positionOffset
        }
    }
}

fun TransformableViewType.setPositionOffset(mOffset: Offset): TransformableViewType {
    when (this) {
        is TransformableViewType.TextTransformable -> {
            this.viewState.positionOffset = mOffset
        }
    }
    return this
}

fun TransformableViewType.getScale(): Float {
    return when (this) {
        is TransformableViewType.TextTransformable -> {
            viewState.scale
        }
    }
}

fun TransformableViewType.setScale(mScale: Float): TransformableViewType {
    when (this) {
        is TransformableViewType.TextTransformable -> {
            this.viewState.scale = mScale
        }
    }
    return this
}
fun TransformableViewType.getRotation(): Float {
    return when (this) {
        is TransformableViewType.TextTransformable -> {
            viewState.rotation
        }
    }
}

fun TransformableViewType.setRotation(mRotation: Float): TransformableViewType {
    when (this) {
        is TransformableViewType.TextTransformable -> {
            this.viewState.rotation = mRotation
        }
    }
    return this
}