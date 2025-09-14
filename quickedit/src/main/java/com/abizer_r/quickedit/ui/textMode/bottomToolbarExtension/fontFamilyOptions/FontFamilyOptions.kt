package com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.fontFamilyOptions

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.theme.ToolBarBackgroundColor
import io.github.abizerr.quickedit.ui.utils.defaultTextColor
import com.abizer_r.quickedit.utils.textMode.FontUtils

@Composable
fun FontFamilyOptions(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ToolBarBackgroundColor,
    selectedFontFamily: FontFamily,
    onItemClicked: (FontFamily) -> Unit
) {

    val fontsList = remember(Unit) {
        FontUtils.getFontItems()
    }

    LazyRow(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        items(
            count = fontsList.size,
            key = { fontsList[it].hashCode() }
        ) {

            SelectableFontItem(
                fontItem = fontsList[it],
                isSelected = fontsList[it].fontFamily == selectedFontFamily,
                onClick = {
                    onItemClicked(fontsList[it].fontFamily)
                }
            )
        }

    }
}

/**
 * Code logic from SelectableToolbarItem
 */
@Composable
fun SelectableFontItem(
    modifier: Modifier = Modifier,
    fontItem: FontItem,
    textSize: TextUnit = 24.sp,
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

    Text(
        modifier = itemModifier
            .clickable { onClick() }
        ,
        text = fontItem.label,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = defaultTextColor(),
            fontFamily = fontItem.fontFamily,
        )
    )

}



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_SelectableFontItem() {
    QuickEditTheme {
        SelectableFontItem(
            fontItem = FontUtils.getFontItems()[0],
            isSelected = true,
            onClick = {}
        )

    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions_FullWidth() {
    QuickEditTheme {
        FontFamilyOptions(
            modifier = Modifier.fillMaxWidth(),
            selectedFontFamily = FontUtils.DefaultFontFamily,
            onItemClicked = {}
        )

    }
}