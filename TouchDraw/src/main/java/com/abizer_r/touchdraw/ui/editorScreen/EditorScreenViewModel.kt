package com.abizer_r.touchdraw.ui.editorScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.utils.editorScreen.EditorScreenUtils
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import javax.inject.Inject

@HiltViewModel
class EditorScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(EditorScreenState())
    val state: StateFlow<EditorScreenState> = _state

    /**
     * Call this function after making sure that the bitmapStack won't be empty
     */
    @Throws(Exception::class)
    fun getCurrentBitmap(): Bitmap {
        val bitmapStack = state.value.bitmapStack
        if (bitmapStack.isEmpty()) {
            throw Exception("EmptyStackException: The bitmapStack should contain at least one bitmap")
        }
        return bitmapStack.peek()
    }

    fun undoEnabled() = state.value.bitmapStack.size > 1   /* there should always be an initial bitmap */
    fun redoEnabled() = state.value.bitmapRedoStack.isNotEmpty()


    fun updateInitialState(initialState: EditorScreenState) {
        _state.update { initialState }
    }

    fun onUndo() {
        _state.update {
            if (undoEnabled()) {
                it.bitmapRedoStack.push(it.bitmapStack.pop())
            }
            it.copy(recompositionTrigger = it.recompositionTrigger + 1)
        }
    }

    fun onRedo() {
        _state.update {
            if (redoEnabled()) {
                it.bitmapStack.push(it.bitmapRedoStack.pop())
            }
            it.copy(recompositionTrigger = it.recompositionTrigger + 1)
        }
    }

}