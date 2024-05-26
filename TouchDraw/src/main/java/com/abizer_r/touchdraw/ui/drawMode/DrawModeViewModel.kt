package com.abizer_r.touchdraw.ui.drawMode

import android.icu.util.Calendar
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.utils.DrawingConstants
import com.abizer_r.touchdraw.utils.setOpacityIfPossible
import com.abizer_r.touchdraw.utils.setShapeTypeIfPossible
import com.abizer_r.touchdraw.utils.setWidthIfPossible
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

    private val _bottomToolbarState = MutableStateFlow(getDefaultBottomToolbarState())
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

    private fun getDefaultBottomToolbarState(
        defaultColorSelected: Color = Color.White
    ): BottomToolbarState {
        val toolbarListItems = getDefaultBottomToolbarItemsList()
        return BottomToolbarState(
            toolbarItems = toolbarListItems,
            selectedItem = toolbarListItems[1],
            selectedColor = defaultColorSelected
        )
    }

    private fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.ColorItem,
            BottomToolbarItem.BrushTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH,
                opacity = DrawingConstants.DEFAULT_STROKE_OPACITY
            ),
            BottomToolbarItem.ShapeTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH,
                opacity = DrawingConstants.DEFAULT_STROKE_OPACITY,
                shapeType = ShapeType.LINE
            ),
            BottomToolbarItem.EraserTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH
            ),
            BottomToolbarItem.TextTool()
        )
    }
}