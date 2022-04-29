package dev.forcecodes.hov.ui.details

import dev.forcecodes.hov.core.UiEvent

sealed class LoadUiActions : UiEvent {
    data class LoadAll(val name: String) : LoadUiActions()
    object Refresh : LoadUiActions()
}