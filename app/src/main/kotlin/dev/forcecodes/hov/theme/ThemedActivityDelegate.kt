package dev.forcecodes.hov.theme

import dev.forcecodes.hov.data.theme.Theme
import kotlinx.coroutines.flow.StateFlow

interface ThemedActivityDelegate {
    /**
     * Allows observing of the current theme
     */
    val theme: StateFlow<Theme>

    /**
     * Allows querying of the current theme synchronously
     */
    val currentTheme: Theme
}