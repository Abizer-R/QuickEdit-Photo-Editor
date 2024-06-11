package com.abizer_r.sketchdraft.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeState
import com.abizer_r.touchdraw.ui.editorScreen.EditorScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {


    /**
     *
     *
     *
     *
     * Sharing data between compose navigation screens = https://www.youtube.com/watch?v=h61Wqy3qcKg&ab_channel=PhilippLackner
     *
     *
     *
     */

    private val _editorScreenState = MutableStateFlow(EditorScreenState())
    val editorScreenState: StateFlow<EditorScreenState> = _editorScreenState

    val undoEnabled get() = editorScreenState.value.bitmapStack.size > 1
    val redoEnabled get() = editorScreenState.value.bitmapRedoStack.size > 0
    val currentBitmap get() = editorScreenState.value.bitmapStack.peek()

    fun addBitmapToStack(bitmap: Bitmap) {
        _editorScreenState.update {
            it.bitmapStack.push(bitmap)
            it.bitmapRedoStack.clear()
            it.copy(recompositionTrigger = it.recompositionTrigger + 1)
        }
    }

    fun onUndo() {
        _editorScreenState.update {
            if (undoEnabled) {
                it.bitmapRedoStack.push(it.bitmapStack.pop())
            }
            it.copy(recompositionTrigger = it.recompositionTrigger + 1)
        }
    }

    fun onRedo() {
        _editorScreenState.update {
            if (redoEnabled) {
                it.bitmapStack.push(it.bitmapRedoStack.pop())
            }
            it.copy(recompositionTrigger = it.recompositionTrigger + 1)
        }
    }

//    val bitmapStack = Stack<Bitmap>()
//    val bitmapRedoStack = Stack<Bitmap>()
//    var bitmap: Bitmap? = null



//    private val _sharedState = MutableStateFlow(0)
//    val sharedState = _sharedState.asStateFlow()
//
//    fun updateState() {
//        _sharedState.value++
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        println("ViewModel cleared")
//    }
}