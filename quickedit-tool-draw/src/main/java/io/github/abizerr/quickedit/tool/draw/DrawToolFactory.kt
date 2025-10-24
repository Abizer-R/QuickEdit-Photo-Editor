package io.github.abizerr.quickedit.tool.draw

import io.github.abizerr.quickedit.tool.draw.session.DrawToolSession
import io.github.abizerr.quickedit.ui.api.ToolFactory
import io.github.abizerr.quickedit.ui.api.ToolParams
import io.github.abizerr.quickedit.ui.api.ToolSession

class DrawToolFactory: ToolFactory {
    override fun create(params: ToolParams): ToolSession = DrawToolSession()
}