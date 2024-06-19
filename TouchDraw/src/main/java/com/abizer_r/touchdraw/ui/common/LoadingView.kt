package com.abizer_r.touchdraw.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView(
    modifier: Modifier,
    progressBarSize: Dp = 48.dp,
    progressBarColor: Color = MaterialTheme.colorScheme.onBackground,
    progressBarStrokeWidth: Dp = 4.dp
) {

    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(progressBarSize)
                .align(Alignment.Center),

            color = progressBarColor,
            strokeWidth = progressBarStrokeWidth,
        )

    }

}