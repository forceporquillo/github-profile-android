package dev.forcecodes.hov.ui

/**
 * Side effects of loading more items from source.
 */
sealed class LoadMoreEffect {
    object Nominal : LoadMoreEffect()
    data class Error(val message: String?) : LoadMoreEffect()
}