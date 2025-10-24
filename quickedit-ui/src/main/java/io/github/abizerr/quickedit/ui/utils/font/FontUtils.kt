package io.github.abizerr.quickedit.ui.utils.font

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import io.github.abizerr.quickedit.ui.R

object FontUtils {



    val eduVicwantFontFamily = FontFamily(
        Font(R.font.edu_vicwant_regular, FontWeight.Companion.Normal),
        Font(R.font.edu_vicwant_bold, FontWeight.Companion.Bold),
    )

    val greyQoFontFamily = FontFamily(
        Font(R.font.grey_qo_regular, FontWeight.Companion.Normal),
    )

    val matemasieFontFamily = FontFamily(
        Font(R.font.matemasie_regular, FontWeight.Companion.Bold),
    )

    val moderusticFontFamily = FontFamily(
        Font(R.font.moderustic_regular, FontWeight.Companion.Normal),
        Font(R.font.moderustic_bold, FontWeight.Companion.Bold),
    )

    val montserratFontFamily = FontFamily(
        Font(R.font.montserrat_regular, FontWeight.Companion.Normal),
        Font(R.font.montserrat_italic, FontWeight.Companion.Normal, FontStyle.Companion.Italic),
        Font(R.font.montserrat_bold, FontWeight.Companion.Bold),
        Font(R.font.montserrat_bold_italic, FontWeight.Companion.Bold, FontStyle.Companion.Italic),
    )

    val newAmsterdamFontFamily = FontFamily(
        Font(R.font.new_amsterdam_regular, FontWeight.Companion.Bold),
    )

    val oswaldFontFamily = FontFamily(
        Font(R.font.oswald_regular, FontWeight.Companion.Normal),
        Font(R.font.oswald_bold, FontWeight.Companion.Bold),
    )

    val playwrightFontFamily = FontFamily(
        Font(R.font.playwrite_regular, FontWeight.Companion.Normal),
        Font(R.font.playwrite_italic, FontWeight.Companion.Normal, FontStyle.Companion.Italic),
    )

    val poppinsFontFamily = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Companion.Normal),
        Font(R.font.poppins_italic, FontWeight.Companion.Normal, FontStyle.Companion.Italic),
        Font(R.font.poppins_bold, FontWeight.Companion.Bold),
        Font(R.font.poppins_bold_italic, FontWeight.Companion.Bold, FontStyle.Companion.Italic),
    )

    val robotoFontFamily = FontFamily(
        Font(R.font.roboto_regular, FontWeight.Companion.Normal),
        Font(R.font.roboto_italic, FontWeight.Companion.Normal, FontStyle.Companion.Italic),
        Font(R.font.roboto_bold, FontWeight.Companion.Bold),
        Font(R.font.roboto_bold_italic, FontWeight.Companion.Bold, FontStyle.Companion.Italic),
    )

    val tekoFontFamily = FontFamily(
        Font(R.font.teko_regular, FontWeight.Companion.Normal),
        Font(R.font.teko_bold, FontWeight.Companion.Bold),
    )

    val DefaultFontFamily = poppinsFontFamily

    fun getFontItems(): List<FontItem> {
        val fontSet = arrayListOf(
            DefaultFontFamily.getFontItem()
        )

        fontSet.add(eduVicwantFontFamily.getFontItem())
        fontSet.add(greyQoFontFamily.getFontItem())
        fontSet.add(moderusticFontFamily.getFontItem())
        fontSet.add(newAmsterdamFontFamily.getFontItem())   // bold doesn't work
        fontSet.add(oswaldFontFamily.getFontItem())
        fontSet.add(matemasieFontFamily.getFontItem())      // bold doesn't work
        fontSet.add(playwrightFontFamily.getFontItem())
        fontSet.add(poppinsFontFamily.getFontItem())
        fontSet.add(robotoFontFamily.getFontItem())
        fontSet.add(tekoFontFamily.getFontItem())


        return fontSet.distinct()
    }

    private fun getLabel(fontFamily: FontFamily): String {
        return when (fontFamily) {
            eduVicwantFontFamily -> "EduVicwant"
            greyQoFontFamily -> "greyQo"
            moderusticFontFamily -> "moderustic"
            newAmsterdamFontFamily -> "newAmsterdam"
            oswaldFontFamily -> "oswald"
            matemasieFontFamily -> "matemasie"
            playwrightFontFamily -> "playwright"
            poppinsFontFamily -> "poppins"
            robotoFontFamily -> "roboto"
            tekoFontFamily -> "teko"
            else -> "unknown font"
        }
    }

    private fun FontFamily.getFontItem() = FontItem(
        fontFamily = this,
        label = getLabel(this)
    )
}