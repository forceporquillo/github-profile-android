@file:Suppress("unused")

package dev.forcecodes.hov.initializer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import dev.forcecodes.hov.core.internal.Logger

class TimberInitializer : Initializer<Unit> {

    @SuppressLint("LogNotTimber")
    override fun create(context: Context) {
        Log.d("TimberInitializer", "create")
        Logger.plant(Logger.DebugTree())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}