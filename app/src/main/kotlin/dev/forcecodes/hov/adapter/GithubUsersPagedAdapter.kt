package dev.forcecodes.hov.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.core.model.UserUiModel
import dev.forcecodes.hov.databinding.UsersItemLayoutBinding
import dev.forcecodes.hov.extensions.executeAfter

internal class GithubUsersPagedAdapter(
    private val listener: OnClickGithubUserListener,
    private val requestNextPage: (Int) -> Unit,
) : PaginatedAdapter<UserUiModel, GithubUsersPagedAdapter.UserVieHolder>(UserUiModelComparator) {

    private var layoutManager: LinearLayoutManager? = null

    internal fun withLoadStateAdapter(
        footer: LoadStateFooterAdapter
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.source.append
        }
        // workaround fix for paginated data that starts at the middle of the list.
        // https://issuetracker.google.com/issues/186560106#comment5
        var firstLoad = true
        addOnPagesUpdatedListener {
            if (itemCount > 0 && firstLoad) {
                layoutManager?.scrollToPositionWithOffset(0, 0)
                firstLoad = false
            }
        }
        return ConcatAdapter(this, footer)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = initLayoutManagerIfNull(recyclerView)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            // simple workaround implementation that mimics the approach of paging library.
            if (closestItemToLastPosition(totalItemCount, visibleItemCount, lastVisibleItem)) {
                // instead of retrieving the api response header that links to the next page.
                // (e.g.) Link: https://api.github.com/users?since=46
                // see docs: https://docs.github.com/en/rest/users/users
                // we can manually hook it up by retrieving the last item
                // and append its user id to the API query params.
                if (snapshot().isEmpty() && itemCount <= 0) {
                    return
                }
                val lastIndex = try {
                    snapshot().lastIndex
                } catch (e: IndexOutOfBoundsException) {
                    0 // reset to top
                }
                getItem(lastIndex)?.id?.let(requestNextPage::invoke)
            }
        }
    }

    /**
     * Coerces the index in the list, including its footer when necessary.
     *
     * Checks whether if it reached the closest bottom item of the list. This is backed by an additional
     * [NEXT_PAGE_THRESHOLD] so it can advance the request upon reaching to the end of the list.
     */
    private fun closestItemToLastPosition(
        totalItemCount: Int, visibleItemCount: Int, lastVisibleItem: Int
    ): Boolean {
       return visibleItemCount + lastVisibleItem + NEXT_PAGE_THRESHOLD >= totalItemCount
    }

    fun interface OnClickGithubUserListener {
        fun onClick(info: Pair<Int, String>)
    }

    internal inner class UserVieHolder(
        binding: UsersItemLayoutBinding
    ) : AbstractPaginatedViewHolder<UsersItemLayoutBinding, UserUiModel>(binding) {

        override fun onItemClick(data: UserUiModel) {
            (data as? UserUiModel.User)?.let {
                if (!it.name.isNullOrEmpty()) {
                    listener.onClick(Pair(it.id, it.name!!))
                }
            }
        }

        override fun bind(data: UserUiModel) {
            super.bind(data)
            binding.executeAfter { uiModel = data as UserUiModel.User }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
        layoutManager = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(onScrollListener)
        initLayoutManagerIfNull(recyclerView)
    }

    override fun viewHolder(parent: ViewGroup): UserVieHolder {
        val binding = parent.viewBinding(UsersItemLayoutBinding::inflate)
        return UserVieHolder(binding)
    }

    private fun initLayoutManagerIfNull(recyclerView: RecyclerView): LinearLayoutManager {
        if (layoutManager == null) {
            layoutManager = recyclerView.layoutManager as LinearLayoutManager
        }
        return layoutManager ?: throw NullPointerException()
    }

    companion object {
        /**
         * Visible threshold until force reload.
         *
         * Note: Increase the value if not required to show progress bar when fetching new data as possible.
         */
        private const val NEXT_PAGE_THRESHOLD = 0
    }
}

