package com.abizer_r.quickedit.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.abizer_r.quickedit.R

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionPreviewExtension(
    content: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Unit
) {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            content(this)
        }
    }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes stringRes: Int) {
    Toast.makeText(this, getString(stringRes), Toast.LENGTH_SHORT).show()
}

fun Context.defaultErrorToast() {
    Toast.makeText(this, this.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
}