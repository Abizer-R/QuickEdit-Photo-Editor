package com.abizer_r.quickedit.utils.effectsMode

import android.graphics.Bitmap

object BitmapToneCurveFilter {
    fun apply(original: Bitmap, curve: AcvToneCurveParser.Curve): Bitmap {
        val rLut = AcvToneCurveParser.interpolate(curve.r)
        val gLut = AcvToneCurveParser.interpolate(curve.g)
        val bLut = AcvToneCurveParser.interpolate(curve.b)

        val w = original.width
        val h = original.height
        val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        val pixels = IntArray(w * h)
        original.getPixels(pixels, 0, w, 0, 0, w, h)

        for (i in pixels.indices) {
            val c = pixels[i]
            val a = c ushr 24
            val r = rLut[(c shr 16) and 0xFF]
            val g = gLut[(c shr 8) and 0xFF]
            val b = bLut[c and 0xFF]
            pixels[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        result.setPixels(pixels, 0, w, 0, 0, w, h)
        return result
    }
}