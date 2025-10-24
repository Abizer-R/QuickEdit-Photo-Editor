package com.abizer_r.quickedit.ui.drawMode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.abizerr.quickedit.tool.draw.models.DrawToolItem
import io.github.abizerr.quickedit.tool.draw.models.setOpacityIfPossible
import io.github.abizerr.quickedit.tool.draw.models.setShapeTypeIfPossible
import io.github.abizerr.quickedit.tool.draw.models.setWidthIfPossible
import io.github.abizerr.quickedit.tool.draw.ui.DrawModeEvent
import io.github.abizerr.quickedit.tool.draw.ui.DrawModeState
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            is DrawModeEvent.UpdateToolbarExtensionVisibility -> {
                _state.update { it.copy(showBottomToolbarExtension = event.isVisible) }
            }
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

            is DrawModeEvent.OnToolbarItemClicked -> {
                handleToolbarItemClicked(event.toolbarItem)
            }

            is DrawModeEvent.UpdateOpacity -> {
                _state.update { it.copy(
                    selectedTool = it.selectedTool.setOpacityIfPossible(event.newOpacity),
                    recompositionTrigger = it.recompositionTrigger + 1
                ) }
            }

            is DrawModeEvent.UpdateWidth -> {
                _state.update { it.copy(
                    selectedTool = it.selectedTool.setWidthIfPossible(event.newWidth),
                    recompositionTrigger = it.recompositionTrigger + 1
                ) }
            }

            is DrawModeEvent.UpdateShapeType -> {
                _state.update { it.copy(
                    selectedTool = it.selectedTool.setShapeTypeIfPossible(event.newShapeType),
                    recompositionTrigger = it.recompositionTrigger + 1
                ) }
            }
        }
    }

    private fun handleToolbarItemClicked(selectedItem: DrawToolItem) = viewModelScope.launch {
        when (selectedItem) {
            is DrawToolItem.ColorItem -> {
                _state.update {
                    it.copy(showColorPicker = it.showColorPicker.not())
                }
            }

            // Clicked on already selected item
            state.value.selectedTool -> {
                if (selectedItem != DrawToolItem.PanItem) {
                    _state.update {
                        it.copy(showBottomToolbarExtension = it.showBottomToolbarExtension.not())
                    }
                }
            }

            // clicked on another item
            else -> {
                if (state.value.showBottomToolbarExtension) {
                    // Collapse toolbarExtension and change current item after DELAY
                    _state.update { it.copy(showBottomToolbarExtension = false) }
                    delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION.toLong())
                }
                _state.update { it.copy(selectedTool = selectedItem) }
                if (selectedItem != DrawToolItem.PanItem) {
                    // open toolbarExtension for new item
                    _state.update { it.copy(showBottomToolbarExtension = true) }
                }
            }
        }
    }
}