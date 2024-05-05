package com.abizer_r.touchdraw.ui.editorScreen.topToolbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor

@Composable
fun TopToolBar(
    modifier: Modifier,
    enableUndo: Boolean = false,
    enableRedo: Boolean = false,
    onUndo: () -> Unit,
    onRedo: () -> Unit
) {
    Row(
        modifier = modifier.background(ToolBarBackgroundColor),
        horizontalArrangement = Arrangement.Center
    ) {
        /**
         * Tool Item: UNDO
         */
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(32.dp)
                .clickable {
                    onUndo()
                },
            contentDescription = null,
            imageVector = Icons.Default.Undo,
            colorFilter = ColorFilter.tint(
                color = if (enableUndo) {
                    MaterialTheme.colorScheme.onBackground
                } else Color.DarkGray
            )
        )

        /**
         * Tool Item: REDO
         */
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(32.dp)
                .clickable {
                    onRedo()
                },
            contentDescription = null,
            imageVector = Icons.Default.Redo,
            colorFilter = ColorFilter.tint(
                color = if (enableRedo) {
                    MaterialTheme.colorScheme.onBackground
                } else Color.DarkGray
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopToolbar() {
    SketchDraftTheme {
        TopToolBar(
            modifier = Modifier.fillMaxWidth(),
            enableUndo = true,
            onUndo = {},
            onRedo = {}
        )
    }
}