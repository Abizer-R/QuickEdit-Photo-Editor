package com.abizer_r.touchdraw.ui.textMode

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeEvent
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeState
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.getId
import com.abizer_r.touchdraw.ui.transformableViews.getIsSelected
import com.abizer_r.touchdraw.ui.transformableViews.getPositionOffset
import com.abizer_r.touchdraw.ui.transformableViews.getRotation
import com.abizer_r.touchdraw.ui.transformableViews.getScale
import com.abizer_r.touchdraw.ui.transformableViews.setIsSelected
import com.abizer_r.touchdraw.ui.transformableViews.setPositionOffset
import com.abizer_r.touchdraw.ui.transformableViews.setRotation
import com.abizer_r.touchdraw.ui.transformableViews.setScale
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
                    it.copy(textFieldValue = event.textInput)
                }
            }

            is TextModeEvent.UpdateTransformableViewsList -> {
                _state.update {
                    it.copy(
                        transformableViewsList = event.list,
                        recompositionTrigger = it.recompositionTrigger + 1
                    )
                }
            }

            is TextModeEvent.AddTransformableTextView -> {
                val newList = state.value.transformableViewsList.also { list ->
                    list.add(event.view)
                }
                _state.update {it.copy(transformableViewsList = newList) }
                updateViewSelection(selectedViewId = event.view.getId())
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

    fun onTransformableBoxEvent(mEvent: TransformableBoxEvents) {
        val stateList = state.value.transformableViewsList
        val viewItem = stateList.find { it.getId() == mEvent.id } ?: return
        if (viewItem.getIsSelected().not()) {
            updateViewSelection(viewItem.getId())
        }
        when(mEvent) {
            is TransformableBoxEvents.OnDrag -> {
                viewItem.setPositionOffset(
                    mOffset = viewItem.getPositionOffset() + mEvent.dragAmount
                )
            }

            is TransformableBoxEvents.OnZoom -> {
                viewItem.setScale(
                    mScale = (viewItem.getScale() * mEvent.zoomAmount).coerceIn(0.5f, 5f)
                )
            }

            is TransformableBoxEvents.OnRotate -> {
                viewItem.setRotation(
                    mRotation = viewItem.getRotation() + mEvent.rotationChange
                )
            }
        }
        onEvent(TextModeEvent.UpdateTransformableViewsList(stateList))
    }

    fun updateViewSelection(selectedViewId: String? = null) {
        val transformableViewsList = state.value.transformableViewsList
        transformableViewsList.forEach {
            val isSelected = if (selectedViewId != null) {
                it.getId() == selectedViewId
            } else false
            it.setIsSelected(isSelected)
        }
        _state.update {
            it.copy(
                transformableViewsList = transformableViewsList,
                recompositionTrigger = it.recompositionTrigger + 1
            )
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