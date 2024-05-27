package com.abizer_r.touchdraw.ui.textMode

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeEvent
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeState
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.utils.drawMode.DrawingConstants
import com.abizer_r.touchdraw.utils.drawMode.setOpacityIfPossible
import com.abizer_r.touchdraw.utils.drawMode.setShapeTypeIfPossible
import com.abizer_r.touchdraw.utils.drawMode.setWidthIfPossible
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TextModeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TextModeState())
    val state: StateFlow<TextModeState> = _state

    private val _bottomToolbarState = MutableStateFlow(TextModeUtils.getDefaultBottomToolbarState())
    val bottomToolbarState: StateFlow<BottomToolbarState> = _bottomToolbarState

    var shouldRequestFocus = false
        private set

//    private var lastCaptureRequest: Long = Calendar.getInstance().timeInMillis

    fun onEvent(event: TextModeEvent) {
        when (event) {
            is TextModeEvent.ShowTextField -> {
                _state.update { it.copy(
                    isTextFieldVisible = true,
                    textFieldValue = ""
                ) }
            }
            is TextModeEvent.HideTextField -> {
                shouldRequestFocus = false
                _state.update { it.copy(isTextFieldVisible = false) }
            }

            is TextModeEvent.UpdateTextFieldValue -> {
                _state.update {
                    it.copy(textFieldValue = event.textInput
                    )
                }
            }
//            is DrawModeEvent.ToggleColorPicker -> {
//                _state.update {
//                    it.copy(showColorPicker = it.showColorPicker.not())
//                }
//                _bottomToolbarState.update {
//                    it.copy(selectedColor = event.selectedColor ?: it.selectedColor)
//                }
//            }


            else -> {}
        }
    }


    fun onBottomToolbarEvent(event: BottomToolbarEvent) {
        when (event) {
            is BottomToolbarEvent.OnItemClicked -> {
                onBottomToolbarItemClicked(event.toolbarItem)
            }

//            is BottomToolbarEvent.UpdateOpacity -> {
//                _bottomToolbarState.update {
//                    it.copy(
//                        selectedItem = it.selectedItem.setOpacityIfPossible(event.newOpacity),
//                        recompositionTriggerValue = it.recompositionTriggerValue + 1
//                    )
//                }
//            }
//
//            is BottomToolbarEvent.UpdateWidth -> {
//                _bottomToolbarState.update {
//                    it.copy(
//                        selectedItem = it.selectedItem.setWidthIfPossible(event.newWidth),
//                        recompositionTriggerValue = it.recompositionTriggerValue + 1
//                    )
//                }
//            }
//
//            is BottomToolbarEvent.UpdateShapeType -> {
//                _bottomToolbarState.update {
//                    it.copy(
//                        selectedItem = it.selectedItem.setShapeTypeIfPossible(event.newShapeType),
//                        recompositionTriggerValue = it.recompositionTriggerValue + 1
//                    )
//                }
//            }

            else -> {}
        }
    }

    private fun onBottomToolbarItemClicked(selectedItem: BottomToolbarItem) {
        when (selectedItem) {
            is BottomToolbarItem.AddItem -> {
                shouldRequestFocus = true
                onEvent(TextModeEvent.ShowTextField)
            }

            else -> {}
        }
    }
}