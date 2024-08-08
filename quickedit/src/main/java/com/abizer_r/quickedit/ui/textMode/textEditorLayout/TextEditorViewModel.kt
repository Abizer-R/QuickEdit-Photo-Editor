package com.abizer_r.quickedit.ui.textMode.textEditorLayout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TextEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _editorState = MutableStateFlow(
        TextEditorState(textStateId = UUID.randomUUID().toString())
    )
    val editorState: StateFlow<TextEditorState> = _editorState

    fun updateInitialState(initialState: TextEditorState) {
        _editorState.update { initialState }
    }

    fun onEvent(event: TextEditorEvent) = viewModelScope.launch {
        when (event) {

            is TextEditorEvent.SelectTextColor -> {
                _editorState.update { it.copy( selectedColor = event.color) }
            }
            is TextEditorEvent.SelectTextAlign -> {
                _editorState.update { it.copy(textAlign = event.textAlign)}
            }
            is TextEditorEvent.UpdateTextFieldValue -> {
                _editorState.update { it.copy(text = event.textInput) }
            }

        }
    }

}