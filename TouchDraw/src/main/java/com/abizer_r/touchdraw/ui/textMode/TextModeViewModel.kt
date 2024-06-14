package com.abizer_r.touchdraw.ui.textMode

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextModeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TextModeState())
    val state: StateFlow<TextModeState> = _state

    private val _bottomToolbarState = MutableStateFlow(TextModeUtils.getDefaultBottomToolbarState())
    val bottomToolbarState: StateFlow<BottomToolbarState> = _bottomToolbarState

    init {
//        debugTrackViewListSize()
    }

    private fun debugTrackViewListSize() {
        GlobalScope.launch {
            while (true) {
                delay(1000)
                Log.e("TEST_TEXT_MODE", ": viewList size = ${state.value.transformableViewStateList.size}", )
            }
        }
    }

    var shouldGoToNextScreen = false

    fun handleStateBeforeCaptureScreenshot() {
        shouldGoToNextScreen = true
        updateViewSelection(null)
    }

    fun onEvent(event: TextModeEvent) {
        when (event) {

            is TextModeEvent.ShowTextField -> {
                Log.e("TEST_BLUR", "PlaceHolder Text: ", )
                _state.update { it.copy(
                    textFieldState = event.textFieldState.copy(
                        isVisible = true,
                        shouldRequestFocus = true
                    )
                ) }
            }

            is TextModeEvent.SelectTextColor -> {
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        selectedColorIndex = event.index
                    )
                ) }
            }

            is TextModeEvent.SelectTextAlign -> {
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        textAlign = event.textAlign
                    )
                )}
            }

            is TextModeEvent.UpdateTextFont -> {
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        textFont = event.textFont
                    )
                )}
            }

            is TextModeEvent.HideTextField -> {
                _state.update { it.copy(
                    textFieldState = it.textFieldState.copy(
                        isVisible = false,
                        shouldRequestFocus = false
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

        }
    }

    fun onTransformableBoxEvent(mEvent: TransformableBoxEvents) {
        val stateList = state.value.transformableViewStateList
        val viewItem = stateList.find { it.id == mEvent.id } ?: return
        when(mEvent) {
            is TransformableBoxEvents.OnDrag -> {
                viewItem.positionOffset += mEvent.dragAmount
            }

            is TransformableBoxEvents.OnZoom -> {
                viewItem.scale = (viewItem.scale * mEvent.zoomAmount).coerceIn(0.5f, 5f)
            }

            is TransformableBoxEvents.OnRotate -> {
                viewItem.rotation += mEvent.rotationChange
            }

            is TransformableBoxEvents.OnCloseClicked -> {
                stateList.remove(viewItem)
            }

            is TransformableBoxEvents.OnTapped -> {
                // Main objective is to select the view when tapped
                // it is already done above, so doing nothing here
                if (viewItem.isSelected && mEvent.textViewState != null) {
                    val currTextFieldState = state.value.textFieldState
                    onEvent(
                        TextModeEvent.ShowTextField(
                            textFieldState = currTextFieldState.copy(
                                textStateId = mEvent.id,
                                text = mEvent.textViewState.text,
                                textAlign = mEvent.textViewState.textAlign,
                                selectedColorIndex = currTextFieldState.getIndexFromColor(
                                    mEvent.textViewState.textColor
                                )
                            )
                        )
                    )
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

            else -> {}
        }
    }

    private fun onBottomToolbarItemClicked(selectedItem: BottomToolbarItem) {
        when (selectedItem) {
            is BottomToolbarItem.AddItem -> {
                onEvent(TextModeEvent.ShowTextField(TextModeState.TextFieldState()))
            }

            else -> {}
        }
    }
}