package com.abizer_r.quickedit.ui.common.toolbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.theme.QuickEditTheme

@Composable
fun SelectableToolbarItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    itemSize: Dp = 24.dp,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val itemModifier = if (isSelected) {
        modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color.DarkGray)
            .padding(8.dp)
    } else {
        modifier.padding(8.dp)
    }

    Image(
        imageVector = imageVector,
        contentDescription = null,
        modifier = itemModifier
            .size(itemSize)
            .clickable {
                onClick()
            },
        colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_SelectableToolbarItem() {
    QuickEditTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            SelectableToolbarItem(
                itemSize = 24.dp,
                isSelected = true,
                imageVector = Icons.Default.FormatStrikethrough,
                onClick = {}
            )

        }
    }
}