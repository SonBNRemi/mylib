package com.sonbn.remi.mylib.ext

import com.sonbn.remi.mylib.BuildConfig

fun runTryCatch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()
    }
}