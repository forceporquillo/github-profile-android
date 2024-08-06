package dev.forcecodes.android.gitprofile.ui

/**
 * Side effects of loading more items from source.
 */
sealed class LoadMoreEffect {
    data object Nominal : LoadMoreEffect()
    data class Error(val message: String?) : LoadMoreEffect()
}