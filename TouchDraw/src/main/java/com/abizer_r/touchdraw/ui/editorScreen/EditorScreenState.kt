package com.abizer_r.touchdraw.ui.editorScreen

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.components.util.ColorUtils
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState
import java.util.Stack

data class EditorScreenState(
    val bitmapStack: Stack<Bitmap> = Stack(),
    val bitmapRedoStack: Stack<Bitmap> = Stack(),
    val recompositionTrigger: Long = 0
)