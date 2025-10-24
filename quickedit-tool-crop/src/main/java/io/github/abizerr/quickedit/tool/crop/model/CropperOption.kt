package io.github.abizerr.quickedit.tool.crop.model

import java.util.UUID

data class CropperOption(
    val id: String = UUID.randomUUID().toString(),
    val aspectRatioX: Float,
    val aspectRatioY: Float,
    val label: String
)