package com.abizer_r.quickedit.ui.editorScreen

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import java.util.Stack

@Immutable
data class EditorScreenState(
    val bitmapStack: Stack<Bitmap> = Stack(),
    val bitmapRedoStack: Stack<Bitmap> = Stack(),
    val recompositionTrigger: Long = 0
)