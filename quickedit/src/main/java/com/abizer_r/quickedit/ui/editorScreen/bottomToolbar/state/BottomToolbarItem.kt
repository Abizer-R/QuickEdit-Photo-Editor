package com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.quickedit.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.caseOptions.TextCaseType
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions.TextStyleAttr

@Immutable
sealed class BottomToolbarItem {
    // ---------- EditorScreen Items - START
    object CropMode : BottomToolbarItem()
    object DrawMode : BottomToolbarItem()
    object TextMode : BottomToolbarItem()
    object EffectsMode : BottomToolbarItem()
    // ---------- EditorScreen Items - End


    // ---------- DrawModeScreen Items - START
    object ColorItem : BottomToolbarItem()
    object PanItem : BottomToolbarItem()
    class BrushTool(var width: Float, var opacity: Float) : BottomToolbarItem()
    class ShapeTool(var width: Float, var opacity: Float, var shapeType: ShapeType) :
        BottomToolbarItem()

    class EraserTool(var width: Float) : BottomToolbarItem()
    // ---------- DrawModeScreen Items - End


    // ---------- TextModeScreen Items - START
    object AddItem : BottomToolbarItem()
    class TextFormat(
        var textStyleAttr: TextStyleAttr,
        var textCaseType: TextCaseType,
        var textAlign: TextAlign,
    ) : BottomToolbarItem()
    class TextFontFamily(var textFontFamily: FontFamily) : BottomToolbarItem()
    // ---------- TextModeScreen Items - End


    object NONE : BottomToolbarItem()
}