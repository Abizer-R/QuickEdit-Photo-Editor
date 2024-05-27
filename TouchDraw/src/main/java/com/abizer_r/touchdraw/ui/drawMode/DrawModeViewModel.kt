package com.abizer_r.touchdraw.ui.drawMode

import android.icu.util.Calendar
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeState
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.utils.drawMode.DrawModeUtils
import com.abizer_r.touchdraw.utils.drawMode.setOpacityIfPossible
import com.abizer_r.touchdraw.utils.drawMode.setShapeTypeIfPossible
import com.abizer_r.touchdraw.utils.drawMode.setWidthIfPossible
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DrawModeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DrawModeState())
    val state: StateFlow<DrawModeState> = _state

    private val _bottomToolbarState = MutableStateFlow(DrawModeUtils.getDefaultBottomToolbarState())
    val bottomToolbarState: StateFlow<BottomToolbarState> = _bottomToolbarState

    var shouldGoToNextScreen = false

    private var lastCaptureRequest: Long = Calendar.getInstance().timeInMillis

    fun onEvent(event: DrawModeEvent) {
        when (event) {
            is DrawModeEvent.ToggleColorPicker -> {
                _state.update {
                    it.copy(showColorPicker = it.showColorPicker.not())
                }
                _bottomToolbarState.update {
                    it.copy(selectedColor = event.selectedColor ?: it.selectedColor)
                }
            }

            DrawModeEvent.OnUndo -> {
                _state.update {
                    if (it.pathDetailStack.isNotEmpty()) {
                        it.redoStack.push(it.pathDetailStack.pop())
                    }
                    it.copy(recompositionTrigger = it.recompositionTrigger + 1)
                }
            }

            DrawModeEvent.OnRedo -> {
                _state.update {
                    if (it.redoStack.isNotEmpty()) {
                        it.pathDetailStack.push(it.redoStack.pop())
                    }
                    it.copy(recompositionTrigger = it.recompositionTrigger + 1)
                }
            }

            is DrawModeEvent.AddNewPath -> {
                _state.update {
                    it.pathDetailStack.push(event.pathDetail)
                    it.redoStack.clear()
                    it.copy(recompositionTrigger = it.recompositionTrigger + 1)
                }
            }
        }
    }

    fun onBottomToolbarEvent(event: BottomToolbarEvent) {
        when (event) {
            is BottomToolbarEvent.OnItemClicked -> {
                onBottomToolbarItemClicked(event.toolbarItem)
            }

            is BottomToolbarEvent.UpdateOpacity -> {
                _bottomToolbarState.update {
                    it.copy(
                        selectedItem = it.selectedItem.setOpacityIfPossible(event.newOpacity),
                        recompositionTriggerValue = it.recompositionTriggerValue + 1
                    )
                }
            }

            is BottomToolbarEvent.UpdateWidth -> {
                _bottomToolbarState.update {
                    it.copy(
                        selectedItem = it.selectedItem.setWidthIfPossible(event.newWidth),
                        recompositionTriggerValue = it.recompositionTriggerValue + 1
                    )
                }
            }

            is BottomToolbarEvent.UpdateShapeType -> {
                _bottomToolbarState.update {
                    it.copy(
                        selectedItem = it.selectedItem.setShapeTypeIfPossible(event.newShapeType),
                        recompositionTriggerValue = it.recompositionTriggerValue + 1
                    )
                }
            }

            else -> {}
        }
    }

    private fun onBottomToolbarItemClicked(selectedItem: BottomToolbarItem) {
        when (selectedItem) {
            is BottomToolbarItem.ColorItem -> {
                _state.update {
                    it.copy(showColorPicker = it.showColorPicker.not())
                }
            }

            // Clicked on already selected item
            bottomToolbarState.value.selectedItem -> {
                _state.update {
                    it.copy(showBottomToolbarExtension = it.showBottomToolbarExtension.not())
                }
            }

            // clicked on another item
            else -> {
//                _state.value = mState.copy(
//                    showBottomToolbarExtension = false
//                )
                _bottomToolbarState.update {
                    it.copy(selectedItem = selectedItem)
                }
            }
        }
    }
}