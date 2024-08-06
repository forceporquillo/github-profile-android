package dev.forcecodes.gitprofile.data.extensions

import android.os.Looper

fun <T> checkMainThread(block: () -> T): T =
    if (Looper.myLooper() == Looper.getMainLooper()) {
        throw IllegalStateException("Object initialized on main thread.")
    } else {
        block()
    }