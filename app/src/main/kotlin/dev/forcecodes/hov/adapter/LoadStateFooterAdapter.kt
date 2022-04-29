package dev.forcecodes.hov.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcecodes.hov.R
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.extensions.cancelWhenActive
import dev.forcecodes.hov.databinding.LoadStateItemLayoutBinding
import dev.forcecodes.hov.ui.LoadMoreEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class LoadStateFooterAdapter(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val loadStateEffect: StateFlow<LoadMoreEffect>
) : LoadStateAdapter<LoadStateFooterAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(
        private val binding: LoadStateItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var loadStateJob: Job? = null

        fun bind(loadState: LoadState) {
            Logger.i("OnBind::LoadState: $loadState")

            val isEndOfPage = loadState is LoadState.NotLoading && loadState.endOfPaginationReached

            binding.progressCircular.isVisible = isEndOfPage
            binding.notLoading.isVisible = false

            if (!isEndOfPage) {
                return
            }

            loadStateJob?.cancelWhenActive()
            loadStateJob = lifecycleScope.launch {
                loadStateEffect.debounce(DEBOUNCE_DELAY)
                    .collect { state ->
                        if (state is LoadMoreEffect.Error) {
                            binding.apply {
                                progressCircular.isVisible = false
                                notLoading.isVisible = true
                                notLoading.text = root.context.getString(R.string.paging_end_scroll_message, state.message)
                            }
                        } else {
                            binding.apply {
                                progressCircular.isVisible = false
                                notLoading.isVisible = false
                            }
                        }
                    }
            }
        }
    }

    private companion object {
        private const val DEFAULT_TIMEOUT = 10000L

        // higher debounce buffer
        private const val DEBOUNCE_DELAY = 3000L
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent.viewBinding(LoadStateItemLayoutBinding::inflate))
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.NotLoading
    }
}