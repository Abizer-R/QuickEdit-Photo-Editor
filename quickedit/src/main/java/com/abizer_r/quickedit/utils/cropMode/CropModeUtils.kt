package com.abizer_r.quickedit.utils.editorScreen

import com.abizer_r.quickedit.ui.cropMode.cropperOptions.CropperOption

object CropModeUtils {

    fun getCropperOptionsList(): ArrayList<CropperOption> {

        val cropperOptionList = arrayListOf(
            CropperOption(
                aspectRatioX = -1f,
                aspectRatioY = -1f,
                label = "free"
            ),
            CropperOption(
                aspectRatioX = 1f,
                aspectRatioY = 1f,
                label = "square"
            ),
            CropperOption(
                aspectRatioX = 3f,
                aspectRatioY = 4f,
                label = "3:4"
            ),
            CropperOption(
                aspectRatioX = 9f,
                aspectRatioY = 16f,
                label = "9:16"
            ),
            CropperOption(
                aspectRatioX = 4f,
                aspectRatioY = 3f,
                label = "4:3"
            ),
            CropperOption(
                aspectRatioX = 16f,
                aspectRatioY = 9f,
                label = "16:9"
            ),
        )
        return cropperOptionList
    }
}