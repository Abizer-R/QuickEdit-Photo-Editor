package io.github.abizerr.quickedit.ui.api

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.abizerr.quickedit.engine.api.EditEngine
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot
import io.github.abizerr.quickedit.engine.api.EditedImage
import io.github.abizerr.quickedit.engine.api.SaveFormat
import io.github.abizerr.quickedit.engine.api.Size
import io.github.abizerr.quickedit.engine.impl.DefaultEditEngine
import io.github.abizerr.quickedit.ui.common.AnimatedToolbarContainer
import io.github.abizerr.quickedit.ui.common.TOOLBAR_HEIGHT_MEDIUM
import io.github.abizerr.quickedit.ui.common.TOOLBAR_HEIGHT_SMALL
import io.github.abizerr.quickedit.ui.theme.QuickEditTheme
import io.github.abizerr.quickedit.ui.utils.PreviewUtils
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST
import io.github.abizerr.quickedit.ui.utils.getDummyBitmap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class QuickEditConfig(
    val tools: List<ToolContribution> = emptyList(),
    val maxUndo: Int = 20,
    val defaultFormat: SaveFormat = SaveFormat.Jpeg(90)
)

private enum class UiMode { Editor, FullScreenTool }


private val topToolbarHeight = TOOLBAR_HEIGHT_SMALL
private val bottomToolbarHeight = TOOLBAR_HEIGHT_MEDIUM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickEditEditor(
    image: EditImage?,
    config: QuickEditConfig = QuickEditConfig(),
    engine: EditEngine = rememberEngine(config.maxUndo),
    onSave: (Result<EditedImage>) -> Unit = {}
) {

    var snapshot by remember { mutableStateOf<EditSnapshot?>(null) }
    var viewport by remember { mutableStateOf(IntSize.Zero) }
    var preview by remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()

    var uiMode by remember { mutableStateOf(UiMode.Editor) }
    var editorToolbarsVisible by remember { mutableStateOf(true) }

    // Start/replace session when image changes
    LaunchedEffect(image, engine) {
        snapshot = image?.let { engine.newSession(it) }
    }

    // Render when snapshot, viewport or uiMode changes
    LaunchedEffect(snapshot, viewport, uiMode) {
        if (uiMode != UiMode.Editor) {
            preview = null
            return@LaunchedEffect   // skip preview rendering while a tool is active
        }
        if (viewport.width <= 0 || viewport.height <= 0) return@LaunchedEffect
        val renderResult = engine.render(
            snapshot = snapshot ?: return@LaunchedEffect,
            size = Size(viewport.width, viewport.height)
        )
        preview = renderResult.preview
    }

    val state = snapshot?.let { QuickEditState(it) }

    // Controller wires to the engine's apply/undo/redo
    val controller = remember(engine, snapshot) {
        object : ToolController {
            override fun emit(op: EditOp) {
                scope.launch { snapshot = engine.apply(op) }
            }

            override fun undo() {
                scope.launch { snapshot = engine.apply(EditOp.Undo) }
            }

            override fun redo() {
                scope.launch { snapshot = engine.apply(EditOp.Redo) }
            }

        }
    }

    var selectedToolId: String? by remember(config.tools) {
        mutableStateOf(null)
    }
    val selectedTool: ToolContribution? = remember(selectedToolId, config.tools) {
        config.tools.firstOrNull { it.id == selectedToolId }
    }

    suspend fun goToTool(toolId: String) {
        // 1) hide toolbars
        editorToolbarsVisible = false
        delay(TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
        // 2) switch to tool
        selectedToolId = toolId
        uiMode = UiMode.FullScreenTool
    }

    suspend fun exitTool() {
        // 1) exit tool
        uiMode = UiMode.Editor
        // 2) show toolbars
        editorToolbarsVisible = true
        selectedToolId = null
    }

    fun saveSnapshot() {
        snapshot?.let {
            scope.launch {
                onSave(engine.save(snapshot!!, config.defaultFormat))
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        TopToolbar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            visible = editorToolbarsVisible,
            height = topToolbarHeight,
            engine = engine,
            controller = controller,
            saveEnabled = snapshot != null,
            onSave = { saveSnapshot() }
        )

        BottomToolbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            visible = editorToolbarsVisible,
            height = bottomToolbarHeight,
            selectedToolId = selectedToolId,
            tools = config.tools,
            onToolClicked = { toolId ->
                scope.launch { goToTool(toolId) }
            }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(top = topToolbarHeight, bottom = bottomToolbarHeight)
                .onSizeChanged { viewport = it },
            contentAlignment = Alignment.Center
        ) {
            // Don’t draw the editor preview when a tool is active
            if (uiMode == UiMode.Editor) {
                if (preview != null) {
                    Image(bitmap = preview!!.asImageBitmap(), contentDescription = "Preview")
                } else {
                    Text("Preparing preview…")
                }
            }
        }

        // --- Full-screen tool layer (tool owns its own bars & content) ---
        if (uiMode == UiMode.FullScreenTool && selectedTool != null && state != null) {
            selectedTool.FullScreenTool(
                state = state,
                controller = controller,
                onExit = { scope.launch { exitTool() } }
            )
        }
    }
}

@Composable
private fun TopToolbar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    height: Dp,
    engine: EditEngine,
    controller: ToolController,
    saveEnabled: Boolean,
    onSave: () -> Unit
) {
    AnimatedToolbarContainer(
        toolbarVisible = visible,
        modifier = modifier
    ) {
        Surface(tonalElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "QuickEdit",
                    style = MaterialTheme.typography.headlineSmall
                )
                TextButton(
                    enabled = engine.history.canUndo,
                    onClick = { controller.undo() }
                ) { Text("Undo") }

                TextButton(
                    enabled = engine.history.canRedo,
                    onClick = { controller.redo() }
                ) { Text("Redo") }

                TextButton(
                    enabled = saveEnabled,
                    onClick = onSave
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
private fun BottomToolbar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    height: Dp,
    selectedToolId: String?,
    tools: List<ToolContribution>,
    onToolClicked: (toolId: String) -> Unit
) {
    AnimatedToolbarContainer(
        toolbarVisible = visible,
        modifier = modifier
    ) {
        Surface(tonalElevation = 3.dp) {
            Row(
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tools.forEach { tool ->
                    // Delegate icon rendering to the tool
                    tool.ToolbarIcon(
                        selected = tool.id == selectedToolId,
                        onClick = { onToolClicked(tool.id) }
                    )
                }
            }
        }
    }
}



@Composable
private fun rememberEngine(maxUndo: Int): EditEngine {
    val resolver: ContentResolver = LocalContext.current.contentResolver
    return remember(resolver, maxUndo) {
        DefaultEditEngine(
            maxUndo = maxUndo,
            resolver = resolver
        )
    }
}

@Preview @Composable
private fun PreviewTopToolbar() {
    QuickEditTheme {
        TopToolbar(
            visible = true,
            height = topToolbarHeight,
            engine = DefaultEditEngine(),
            controller = PreviewUtils.getDummyToolController(),
            saveEnabled = true,
            onSave = {}
        )
    }
}

@Preview @Composable
private fun PreviewBottomToolbar() {
    QuickEditTheme {
        val tools = PreviewUtils.getDummyTools()
        BottomToolbar(
            visible = true,
            height = bottomToolbarHeight,
            selectedToolId = tools[0].id,
            tools = tools,
            onToolClicked = { toolId -> }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    QuickEditTheme {
        QuickEditEditor(
            image = EditImage.FromBitmap(getDummyBitmap()),
            config = QuickEditConfig(tools = PreviewUtils.getDummyTools()),
            onSave = {}
        )
    }
}