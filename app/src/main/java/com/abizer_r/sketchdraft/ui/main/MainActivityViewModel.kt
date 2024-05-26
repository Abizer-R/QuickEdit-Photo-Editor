package com.abizer_r.sketchdraft.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {


    /**
     *
     *
     *
     *
     * Sharing data between compose navigation screens = https://www.youtube.com/watch?v=h61Wqy3qcKg&ab_channel=PhilippLackner
     *
     *
     *
     */



//    private val _sharedState = MutableStateFlow(0)
//    val sharedState = _sharedState.asStateFlow()
//
//    fun updateState() {
//        _sharedState.value++
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        println("ViewModel cleared")
//    }
}