package dev.forcecodes.android.gitprofile.ui.details

import dev.forcecodes.gitprofile.core.UiEvent

sealed class DetailsUiEvent : UiEvent {
    data class GetData(val pair: Pair<Int, String>) : DetailsUiEvent()
}