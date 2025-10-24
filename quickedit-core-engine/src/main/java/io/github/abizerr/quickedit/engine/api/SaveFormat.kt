package io.github.abizerr.quickedit.engine.api

sealed interface SaveFormat {
    data class Png(val lossless: Boolean = true) : SaveFormat
    data class Jpeg(val quality: Int = 90) : SaveFormat
    data class WebP(val lossless: Boolean = false, val quality: Int = 90) : SaveFormat
    /**
     * FUTURE: Add new Codecs additively
     * example: Heif, Avif
     */
}
