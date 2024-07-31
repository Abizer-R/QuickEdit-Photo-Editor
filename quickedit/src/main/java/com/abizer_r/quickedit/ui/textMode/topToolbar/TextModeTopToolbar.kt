package com.abizer_r.quickedit.ui.editorScreen.topToolbar

import android.content.res.Configuration
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
fun TextModeTopToolbar(
    modifier: Modifier,
    onCloseClicked: () -> Unit,
    onDoneClicked: () -> Unit
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
                    onCloseClicked()
                },
            contentDescription = null,
            imageVector = Icons.Default.Close,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        /**
         * Tool Item: REDO
         */
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(32.dp)
                .clickable {
                    onDoneClicked()
                },
            contentDescription = null,
            imageVector = Icons.Default.Check,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextModeTopToolbar() {
    SketchDraftTheme {
        TextModeTopToolbar(
            modifier = Modifier.fillMaxWidth(),
            onCloseClicked = {},
            onDoneClicked = {}
        )
    }
}