package com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.caseOptions

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.FormatAlignRight
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.theme.ToolBarBackgroundColor
import com.abizer_r.quickedit.ui.common.toolbar.SelectableToolbarItem
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.alignmentOptions.TextAlignOptions
import com.abizer_r.quickedit.utils.textMode.TextModeUtils

@Composable
fun TextCaseOptions(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ToolBarBackgroundColor,
    selectedTextCase: TextCaseType,
    onItemClicked: (TextCaseType) -> Unit
) {

    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        SelectableToolbarItem(
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_match_case_24),
            isSelected = selectedTextCase == TextCaseType.DEFAULT,
            onClick = {
                onItemClicked(TextCaseType.DEFAULT)
            }
        )

        SelectableToolbarItem(
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_uppercase_24),
            isSelected = selectedTextCase == TextCaseType.UPPER_CASE,
            onClick = {
                onItemClicked(TextCaseType.UPPER_CASE)
            }
        )

        SelectableToolbarItem(
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_lowercase_24),
            isSelected = selectedTextCase == TextCaseType.LOWER_CASE,
            onClick = {
                onItemClicked(TextCaseType.LOWER_CASE)
            }
        )

    }
}



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions() {
    QuickEditTheme {
        TextCaseOptions(
            selectedTextCase = TextCaseType.DEFAULT,
            onItemClicked = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_AlignmentOptions_FullWidth() {
    QuickEditTheme {
        TextCaseOptions(
            modifier = Modifier.fillMaxWidth(),
            selectedTextCase = TextCaseType.DEFAULT,
            onItemClicked = {}
        )

    }
}