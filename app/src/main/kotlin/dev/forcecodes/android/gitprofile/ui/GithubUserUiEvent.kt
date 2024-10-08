package dev.forcecodes.android.gitprofile.ui

import dev.forcecodes.gitprofile.core.UiEvent

/**
 * Intent events
 */
sealed class GithubUserUiEvent : UiEvent {
    data class OnRefresh(val page: Int) : GithubUserUiEvent()
    data class OnLoad(val since: Int) : GithubUserUiEvent()
    data class OnSearchUser(val name: CharSequence): GithubUserUiEvent()
    object OnErrorRetry : GithubUserUiEvent()
    object Reload : GithubUserUiEvent()
}