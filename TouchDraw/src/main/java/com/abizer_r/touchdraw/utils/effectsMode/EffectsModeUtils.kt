package com.abizer_r.touchdraw.utils.editorScreen

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Log
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.effectsMode.effectsPreview.EffectItem
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
                "acv/afterglow.acv",
                "acv/alice_in_wonderland.acv",
                "acv/ambers.acv",
                "acv/aurora.acv",
                "acv/blue_poppies.acv",
                "acv/blue_yellow_field.acv",
                "acv/carousel.acv",
                "acv/cold_desert.acv",
                "acv/cold_heart.acv",
                "acv/digital_film.acv",
                "acv/documentary.acv",
                "acv/electric.acv",
                "acv/ghosts_in_your_head.acv",
                "acv/good_luck_charm.acv",
                "acv/green_envy.acv",
                "acv/hummingbirds.acv",
                "acv/kiss_kiss.acv",
                "acv/left_hand_blues.acv",
                "acv/light_parades.acv",
                "acv/lullabye.acv",
                "acv/moth_wings.acv",
                "acv/moth_wings.acv",
                "acv/old_postcards_01.acv",
                "acv/old_postcards_02.acv",
                "acv/peacock_feathers.acv",
                "acv/pistol.acv",
                "acv/ragdoll.acv",
                "acv/rose_thorns_01.acv",
                "acv/rose_thorns_02.acv",
                "acv/set_you_free.acv",
                "acv/snow_white.acv",
                "acv/toes_in_the_ocean.acv",
                "acv/wild_at_heart.acv",
                "acv/window_warmth.acv")
        )

        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)

        effectList.add(
            EffectItem(
                previewBitmap = bitmap,
                label = "original"
            )
        )

        try {
            gpuImage.setFilter(GPUImageGrayscaleFilter())
            effectList.add(
                EffectItem(
                    previewBitmap = gpuImage.bitmapWithFilterApplied,
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

                effectList.add(
                    EffectItem(
                        previewBitmap = gpuImage.bitmapWithFilterApplied,
                        label = fileName.drop(4).dropLast(4)
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return effectList
    }
}