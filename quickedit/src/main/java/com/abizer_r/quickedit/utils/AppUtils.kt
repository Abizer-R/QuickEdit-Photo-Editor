package com.abizer_r.quickedit.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.math.abs


object AppUtils {

    fun getScreenWidthAndHeight(context: Context): Pair<Int, Int> {
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        return Pair(width, height)
    }

    fun getScreenWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        return width
    }

    fun getScreenHeight(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val height = displayMetrics.heightPixels
        return height
    }

    fun getConstrainOffsetScaleOnly(
        context: Context,
        aspectRatio: Float,
        verticalToolbarPaddingPx: Float,
        scale: Float
    ): Offset {

        val initialWidthPx: Float
        val initialHeightPx: Float

        if (aspectRatio >= 1) {
            initialWidthPx = AppUtils.getScreenWidth(context).toFloat()
            initialHeightPx = (initialWidthPx / aspectRatio)

        } else {
            initialHeightPx = AppUtils.getScreenHeight(context).toFloat() - verticalToolbarPaddingPx
            initialWidthPx = initialHeightPx * aspectRatio
        }

        val canvasWidth = initialWidthPx * scale
        val canvasHeight = canvasWidth / aspectRatio

        val outlierSpaceEachSideHorizontal = abs(canvasWidth - initialWidthPx) / 2
        val outlierSpaceEachSideVertical = abs(canvasHeight - initialHeightPx) / 2

        val horizontalConstrain = initialWidthPx * 0.3f
        val verticalConstrain = initialHeightPx * 0.2f

        val adjustedHorizontalConstrain = horizontalConstrain + outlierSpaceEachSideHorizontal
        val adjustedVerticalConstrain = verticalConstrain + outlierSpaceEachSideVertical

        return Offset(adjustedHorizontalConstrain, adjustedVerticalConstrain)
    }

}