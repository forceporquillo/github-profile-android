package dev.forcecodes.hov.ui.details

import dev.forcecodes.hov.core.UiEvent

sealed class DetailsUiEvent : UiEvent {
    data class GetData(val pair: Pair<Int, String>) : DetailsUiEvent()
}