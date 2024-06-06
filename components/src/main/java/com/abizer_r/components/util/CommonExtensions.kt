package com.abizer_r.components.util

import android.content.Context
import android.widget.Toast
import com.abizer_r.components.R

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.defaultErrorToast() {
    Toast.makeText(this, this.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
}