package com.abizer_r.touchdraw.ui.drawMode

import android.icu.util.Calendar
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeState
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
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

    var shouldGoToNextScreen = false
    // shows the icon initially, then show selected color
    var showColorPickerIconInToolbar = true

    fun handleStateBeforeCaptureScreenshot() {
        shouldGoToNextScreen = true
        _state.update {
            it.copy(showBottomToolbarExtension = false)
        }
    }

    fun onEvent(event: DrawModeEvent) {
        when (event) {
            is DrawModeEvent.ToggleColorPicker -> {
                _state.update {
                    it.copy(
                        showColorPicker = it.showColorPicker.not(),
                        selectedColor = event.selectedColor ?: it.selectedColor
                    )
                }
                showColorPickerIconInToolbar = false
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
                _state.update { it.copy(
                    selectedTool = it.selectedTool.setOpacityIfPossible(event.newOpacity),
                    recompositionTrigger = it.recompositionTrigger + 1
                ) }
            }

            is BottomToolbarEvent.UpdateWidth -> {
                _state.update { it.copy(
                    selectedTool = it.selectedTool.setWidthIfPossible(event.newWidth),
                    recompositionTrigger = it.recompositionTrigger + 1
                ) }
            }

            is BottomToolbarEvent.UpdateShapeType -> {
                _state.update { it.copy(
                    selectedTool = it.selectedTool.setShapeTypeIfPossible(event.newShapeType),
                    recompositionTrigger = it.recompositionTrigger + 1
                ) }
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
            state.value.selectedTool -> {
                _state.update {
                    it.copy(showBottomToolbarExtension = it.showBottomToolbarExtension.not())
                }
            }

            // clicked on another item
            else -> {
                _state.update {
                    it.copy(selectedTool = selectedItem)
                }
            }
        }
    }
}