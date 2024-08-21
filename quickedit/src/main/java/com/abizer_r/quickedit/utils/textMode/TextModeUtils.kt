package com.abizer_r.quickedit.utils.textMode

import android.util.Log
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
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
            key(mViewState.id) {
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
                key(mViewState.id) {
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
    }

    fun getBottomToolbarItemsList(
        selectedViewState: TransformableBoxState?,
        selectedItem: BottomToolbarItem? = null
    ): ArrayList<BottomToolbarItem> {

        val toolbarItems: ArrayList<BottomToolbarItem> = arrayListOf()
        toolbarItems.add(BottomToolbarItem.AddItem)
        if (selectedViewState == null || selectedViewState !is TransformableTextBoxState) {
            return toolbarItems
        }

        val textFormatToolItem: BottomToolbarItem.TextFormat =
            if (selectedItem is BottomToolbarItem.TextFormat)
                selectedItem else getTextFormat(selectedViewState)
        toolbarItems.add(textFormatToolItem)

        val textFontFamilyToolItem: BottomToolbarItem.TextFontFamily =
            if (selectedItem is BottomToolbarItem.TextFontFamily)
                selectedItem else getTextFontFamily(selectedViewState)
        toolbarItems.add(textFontFamilyToolItem)

        return toolbarItems
    }

    private fun getTextFormat(viewState: TransformableTextBoxState): BottomToolbarItem.TextFormat {
        return BottomToolbarItem.TextFormat(
            textStyleAttr = viewState.textStyleAttr,
            textCaseType = viewState.textCaseType,
            textAlign = viewState.textAlign
        )
    }

    private fun getTextFontFamily(viewState: TransformableTextBoxState): BottomToolbarItem.TextFontFamily {
        return BottomToolbarItem.TextFontFamily(
            textFontFamily = viewState.textFontFamily
        )
    }

    val DEFAULT_TEXT_ALIGN = TextAlign.Center

    fun getTextAlignOptions() = arrayListOf(
        TextAlign.Start,
        TextAlign.Center,
        TextAlign.End
    )

    fun isTextModeItem(toolbarItem: BottomToolbarItem): Boolean {
        // check if the toolbarItem is one of the types that is added in getBottomToolbarItemsList()
        return toolbarItem is BottomToolbarItem.AddItem ||
                toolbarItem is BottomToolbarItem.TextFormat ||
                toolbarItem is BottomToolbarItem.TextFontFamily
    }

    fun getNewSelectedToolItem(toolbarItems: ArrayList<BottomToolbarItem>, prevSelectedTool: BottomToolbarItem): BottomToolbarItem {
        val newSelectedTool = when (prevSelectedTool) {
            is BottomToolbarItem.TextFormat -> toolbarItems.find { it is BottomToolbarItem.TextFormat }
            is BottomToolbarItem.TextFontFamily -> toolbarItems.find { it is BottomToolbarItem.TextFontFamily }
            else -> BottomToolbarItem.NONE
        }
        return newSelectedTool ?: BottomToolbarItem.NONE
    }
}