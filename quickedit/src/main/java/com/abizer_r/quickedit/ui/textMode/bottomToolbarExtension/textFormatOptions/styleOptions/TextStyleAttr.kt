package com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions

data class TextStyleAttr(
    var isBold: Boolean = false,
    var isItalic: Boolean = false,
    var isStrikeThrough: Boolean = false,
    var textDecoration: TextDecoration = TextDecoration.None
)

enum class TextDecoration {
    None, Underline, StrikeThrough
}