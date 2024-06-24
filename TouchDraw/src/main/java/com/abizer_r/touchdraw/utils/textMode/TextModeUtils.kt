package com.abizer_r.touchdraw.utils.textMode

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.transformableViews.TransformableTextBox
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.utils.drawMode.DrawingConstants

object TextModeUtils {

    @Composable
    fun getColorsForTextField(
        cursorColor: Color
    ): TextFieldColors {
        return TextFieldDefaults.colors(
            cursorColor = cursorColor,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent

        )
    }


    @Composable
    fun DrawAllTransformableViews(
        centerAlignModifier: Modifier,
        transformableViewsList: ArrayList<TransformableBoxState>,
        onTransformableBoxEvent: (event: TransformableBoxEvents) -> Unit
    ) {
        transformableViewsList.forEach { mViewState ->
            when (mViewState) {
                is TransformableTextBoxState -> {
                    TransformableTextBox(
                        modifier = centerAlignModifier,
                        viewState = mViewState,
                        onEvent = onTransformableBoxEvent
                    )
                }
            }
        }
    }


    @Composable
    fun BorderForSelectedViews(
        centerAlignModifier: Modifier,
        transformableViewsList: ArrayList<TransformableBoxState>,
        onTransformableBoxEvent: (event: TransformableBoxEvents) -> Unit
    ) {
        transformableViewsList
            .filter { it.isSelected }
            .forEach { mViewState ->
                when (mViewState) {
                    is TransformableTextBoxState -> {
                        TransformableTextBox(
                            modifier = centerAlignModifier,
                            viewState = mViewState,
                            showBorderOnly = true,
                            onEvent = onTransformableBoxEvent
                        )
                    }
                }
            }
    }

    fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.AddItem
        )
    }

    val DEFAULT_TEXT_ALIGN = TextAlign.Center

    fun getTextAlignOptions() = arrayListOf(
        TextAlign.Start,
        TextAlign.Center,
        TextAlign.End
    )
}