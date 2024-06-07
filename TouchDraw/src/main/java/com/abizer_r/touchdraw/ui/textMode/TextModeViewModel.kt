package com.abizer_r.touchdraw.ui.textMode

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.textMode.stateHandling.TextModeEvent
import com.abizer_r.touchdraw.ui.textMode.stateHandling.TextModeState
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
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

    var shouldRequestFocus = true
        private set

    var shouldGoToNextScreen = false

//    private var lastCaptureRequest: Long = Calendar.getInstance().timeInMillis

    fun onEvent(event: TextModeEvent) {
        when (event) {
            is TextModeEvent.ShowTextField -> {
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        isVisible = true,
                        textStateId = event.textFieldState.textStateId,
                        text = event.textFieldState.text,
                        textAlign = event.textFieldState.textAlign,
                        textColor = event.textFieldState.textColor
                    )
                ) }
            }
            is TextModeEvent.HideTextField -> {
                shouldRequestFocus = false
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        isVisible = false
                    )
                ) }
            }

            is TextModeEvent.UpdateTextFieldValue -> {
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        text = event.textInput
                    )
                ) }
            }

            is TextModeEvent.UpdateTransformableViewsList -> {
                _state.update {
                    it.copy(
                        transformableViewStateList = event.list,
                        recompositionTrigger = it.recompositionTrigger + 1
                    )
                }
            }

            is TextModeEvent.AddTransformableTextBox -> {
                val id = event.textBoxState.id
                val existingItem = state.value.transformableViewStateList.find { it.id == id }
                if (existingItem != null) {
                    (existingItem as TransformableTextBoxState).apply {
                        text = event.textBoxState.text
                        textAlign = event.textBoxState.textAlign
                        textColor = event.textBoxState.textColor
                    }
                } else {
                    val newList = state.value.transformableViewStateList.also { list ->
                        list.add(event.textBoxState)
                    }
                    _state.update {it.copy(transformableViewStateList = newList) }
                }

                updateViewSelection(selectedViewId = event.textBoxState.id)
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
        val stateList = state.value.transformableViewStateList
        val viewItem = stateList.find { it.id == mEvent.id } ?: return
        when(mEvent) {
            is TransformableBoxEvents.OnDrag -> {
                viewItem.positionOffset += mEvent.dragAmount
//                viewItem.setPositionOffset(
//                    mOffset = viewItem.getPositionOffset() + mEvent.dragAmount
//                )
            }

            is TransformableBoxEvents.OnZoom -> {
                viewItem.scale = (viewItem.scale * mEvent.zoomAmount).coerceIn(0.5f, 5f)
//                viewItem.setScale(
//                    mScale = (viewItem.getScale() * mEvent.zoomAmount).coerceIn(0.5f, 5f)
//                )
            }

            is TransformableBoxEvents.OnRotate -> {
                viewItem.rotation += mEvent.rotationChange
//                viewItem.setRotation(
//                    mRotation = viewItem.getRotation() + mEvent.rotationChange
//                )
            }

            is TransformableBoxEvents.OnCloseClicked -> {
                stateList.remove(viewItem)
            }

            is TransformableBoxEvents.OnTapped -> {
                // Main objective is to select the view when tapped
                // it is already done above, so doing nothing here
                if (viewItem.isSelected && mEvent.textViewState != null) {
                    shouldRequestFocus = true
                    onEvent(
                        TextModeEvent.ShowTextField(
                        TextModeState.TextFieldState(
                            textStateId = mEvent.id,
                            text = mEvent.textViewState.text,
                            textAlign = mEvent.textViewState.textAlign,
                            textColor = mEvent.textViewState.textColor
                        )
                    ))
                }
            }
        }
        if (viewItem.isSelected.not()) {
            Log.e("TEST_Select", "onTransformableBoxEvent: selecting item ${viewItem.id}", )
            updateViewSelection(viewItem.id)
        }
        onEvent(TextModeEvent.UpdateTransformableViewsList(stateList))
    }

    fun updateViewSelection(selectedViewId: String? = null) {
        val transformableViewsList = state.value.transformableViewStateList
        transformableViewsList.forEach {
            val isSelected = if (selectedViewId != null) {
                it.id == selectedViewId
            } else false
            it.isSelected = isSelected
        }
        _state.update {
            it.copy(
                transformableViewStateList = transformableViewsList,
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
                onEvent(TextModeEvent.ShowTextField(TextModeState.TextFieldState()))
            }

            else -> {}
        }
    }
}