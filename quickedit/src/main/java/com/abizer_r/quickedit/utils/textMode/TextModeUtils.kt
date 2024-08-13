package com.abizer_r.quickedit.utils.textMode

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.caseOptions.TextCaseType
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions.TextStyleAttr
import com.abizer_r.quickedit.ui.transformableViews.TransformableTextBox
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBoxState
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableTextBoxState

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

    fun getDefaultBottomToolbarItemsList(
        selectedViewState: TransformableBoxState?
    ): ArrayList<BottomToolbarItem> {

        val toolbarItems: ArrayList<BottomToolbarItem> = arrayListOf()
        toolbarItems.add(BottomToolbarItem.AddItem)
        if (selectedViewState == null) {
            return toolbarItems
        }

        if (selectedViewState is TransformableTextBoxState) {
            val textFormatToolItem: BottomToolbarItem.TextFormat = getTextFormat(selectedViewState)
            toolbarItems.add(textFormatToolItem)
        }
        return toolbarItems
    }

    private fun getTextFormat(viewState: TransformableTextBoxState): BottomToolbarItem.TextFormat {
        return BottomToolbarItem.TextFormat(
            textStyleAttr = viewState.textStyleAttr,
            textCaseType = viewState.textCaseType,
            textAlign = viewState.textAlign
        )
    }

    val DEFAULT_TEXT_ALIGN = TextAlign.Center

    fun getTextAlignOptions() = arrayListOf(
        TextAlign.Start,
        TextAlign.Center,
        TextAlign.End
    )
}