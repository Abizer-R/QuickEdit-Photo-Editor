package com.abizer_r.quickedit.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abizer_r.quickedit.R

@Composable
fun ErrorView(
    modifier: Modifier,
    errorText: String = stringResource(R.string.something_went_wrong),
    errorFontSize: TextUnit = 48.sp,
) {

    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = errorText,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = errorFontSize
            )
        )
    }

}