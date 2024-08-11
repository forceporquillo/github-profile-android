package dev.forcecodes.android.gitprofile.ui.details

import dev.forcecodes.gitprofile.core.UiEvent
import dev.forcecodes.gitprofile.domain.DataStrategy

sealed class DetailsUiEvent(
    open val data: Pair<Int, String>,
    val strategy: DataStrategy
): UiEvent {

    data class GetData(
        override val data: Pair<Int, String>
    ) : DetailsUiEvent(data, DataStrategy.CacheOverRemote)

    data class Refresh(
        override val data: Pair<Int, String>
    ) : DetailsUiEvent(data, DataStrategy.RemoteOverCache)
}