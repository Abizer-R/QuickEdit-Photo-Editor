package com.abizer_r.touchdraw.utils.editorScreen

import android.content.Context
import android.graphics.Bitmap
import com.abizer_r.touchdraw.ui.effectsMode.effectsPreview.EffectItem
import com.abizer_r.touchdraw.utils.AppUtils
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter

object EffectsModeUtils {

    fun getEffectsPreviewList(
        context: Context, 
        bitmap: Bitmap, 
    ): ArrayList<EffectItem> {
        val effectList = arrayListOf<EffectItem>()

        val filterFile = arrayListOf<String>()
        filterFile.addAll(
            listOf(
                "acv/Fade.acv",
                "acv/Pistol.acv",
                "acv/Cinnamon_darkness.acv",
                "acv/Blue_Poppies.acv",
                "acv/Brighten.acv",
                "acv/Darken.acv",
                "acv/Contrast.acv",
                "acv/Matte.acv",
                "acv/Softness.acv",
                "acv/Carousel.acv",
                "acv/Electric.acv",
                "acv/Peacock_Feathers.acv",
                "acv/Good_Luck_Charm.acv",
                "acv/Lullabye.acv",
                "acv/Moth_Wings.acv",
                "acv/Old_Postcards_1.acv",
                "acv/Old_Postcards_2.acv",
                "acv/Toes_In_The_Ocean.acv",

                // NEW ONES
                "acv/Mark_Galer_Grading.acv",
                "acv/Curve_1.acv",
                "acv/Curve_2.acv",
                "acv/Curve_3.acv",
                "acv/Curve_Le_Fabuleux_Coleur_de_Amelie.acv",
                "acv/Tropical_Beach.acv",
            )
        )

        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)

        effectList.add(
            EffectItem(
                ogBitmap = bitmap,
                previewBitmap = getScaledPreviewBitmap(context, bitmap),
                label = "original"
            )
        )

        try {
            gpuImage.setFilter(GPUImageGrayscaleFilter())
            val grayScaleBitmap = gpuImage.bitmapWithFilterApplied
            effectList.add(
                EffectItem(
                    ogBitmap = grayScaleBitmap,
                    previewBitmap = getScaledPreviewBitmap(context, grayScaleBitmap),
                    label = "grayscale"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        filterFile.forEachIndexed { index, fileName ->
            try {
                val inputFilter = context.assets.open(fileName)
                val gpuFilter = GPUImageToneCurveFilter()
                gpuFilter.setFromCurveFileInputStream(inputFilter)
                inputFilter.close()
                gpuImage.setFilter(gpuFilter)

                val filteredBitmap = gpuImage.bitmapWithFilterApplied
                effectList.add(
                    EffectItem(
                        ogBitmap = filteredBitmap,
                        previewBitmap = getScaledPreviewBitmap(context, filteredBitmap),
                        label = fileName.drop(4).dropLast(4).replace("_", " ")
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return effectList
    }

    fun getScaledPreviewBitmap(context: Context, originalBitmap: Bitmap): Bitmap {

        val screenWidth = AppUtils.getScreenWidth(context)
        val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
        val reqWidth = screenWidth / 3
        val reqHeight = (reqWidth / aspectRatio).toInt()
        return Bitmap.createScaledBitmap(originalBitmap, reqWidth, reqHeight, false)
    }
}