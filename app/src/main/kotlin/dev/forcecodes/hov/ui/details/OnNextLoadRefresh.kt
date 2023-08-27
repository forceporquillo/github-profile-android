package dev.forcecodes.hov.ui.details

import androidx.core.widget.NestedScrollView

/**
 * Sets an [NestedScrollView.OnScrollChangeListener] on this [NestedScrollView] that
 * invokes the given [block] when the view is scrolled to the bottom.
 *
 * @param block The lambda to be invoked when the view is scrolled to the bottom.
 * It takes a [LoadUiActions.Refresh] parameter, which indicates that the view should be refreshed.
 */
internal fun NestedScrollView.onNextRefresh(action: (LoadUiActions.Refresh) -> Unit) {
    setOnScrollChangeListener { view: NestedScrollView, _, scrollY, _, oldScrollY ->
        view.getChildAt(view.childCount - 1).let { lastChildView ->
            if ((scrollY >= (lastChildView.measuredHeight - view.measuredHeight)) && scrollY > oldScrollY) {
                // end of paging reload all
                action.invoke(LoadUiActions.Refresh)
            }
        }
    }
}