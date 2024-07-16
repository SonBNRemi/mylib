package com.sonbn.remi.mylib.ext

import android.view.View
import com.sonbn.remi.mylib.BuildConfig

fun runTryCatch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}