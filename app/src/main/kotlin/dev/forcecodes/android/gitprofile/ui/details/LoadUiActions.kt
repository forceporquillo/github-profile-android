package dev.forcecodes.android.gitprofile.ui.details

import dev.forcecodes.gitprofile.core.UiEvent

sealed class LoadUiActions : UiEvent {
    data class LoadAll(val name: String) : LoadUiActions()
    data object Refresh : LoadUiActions()
}