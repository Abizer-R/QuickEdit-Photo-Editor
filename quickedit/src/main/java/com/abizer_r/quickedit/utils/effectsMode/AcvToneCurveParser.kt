package com.abizer_r.quickedit.utils.effectsMode

import java.io.InputStream
import kotlin.math.roundToInt

object AcvToneCurveParser {
    data class Curve(val r: List<Pair<Float, Float>>, val g: List<Pair<Float, Float>>, val b: List<Pair<Float, Float>>)

    fun parse(input: InputStream): Curve {
        val data = input.readBytes()
        var offset = 0

        fun readShort(): Int {
            val result = ((data[offset].toInt() and 0xFF) shl 8) or (data[offset + 1].toInt() and 0xFF)
            offset += 2
            return result
        }

        readShort() // version
        val totalCurves = readShort()

        val curves = mutableListOf<List<Pair<Float, Float>>>()
        repeat(totalCurves) {
            val pointCount = readShort()
            val points = List(pointCount) {
                val y = readShort().toFloat()
                val x = readShort().toFloat()
                Pair(x / 255f, y / 255f)
            }.sortedBy { it.first }
            curves.add(points)
        }

        return Curve(
            r = curves.getOrNull(1) ?: curves[0],
            g = curves.getOrNull(2) ?: curves[0],
            b = curves.getOrNull(3) ?: curves[0]
        )
    }

    fun interpolate(points: List<Pair<Float, Float>>): IntArray {
        val lut = IntArray(256)
        for (i in 0..255) {
            val x = i / 255f
            val p = points.zipWithNext().firstOrNull { x >= it.first.first && x <= it.second.first }
            val value = when {
                p != null -> {
                    val (x1, y1) = p.first
                    val (x2, y2) = p.second
                    y1 + (x - x1) * (y2 - y1) / (x2 - x1)
                }
                x <= points.first().first -> points.first().second
                else -> points.last().second
            }
            lut[i] = (value.coerceIn(0f, 1f) * 255).roundToInt()
        }
        return lut
    }
}