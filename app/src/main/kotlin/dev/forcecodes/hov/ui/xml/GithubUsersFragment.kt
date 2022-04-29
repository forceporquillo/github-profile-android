package dev.forcecodes.hov.ui.xml

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.hov.R
import dev.forcecodes.hov.adapter.GithubUsersPagedAdapter
import dev.forcecodes.hov.adapter.LoadStateFooterAdapter
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.databinding.FragmentGithubUsersBinding
import dev.forcecodes.hov.extensions.launchWithViewLifecycle
import dev.forcecodes.hov.extensions.launchWithViewLifecycleScope
import dev.forcecodes.hov.ui.GithubUserViewModel
import dev.forcecodes.hov.ui.MainActivityViewModel
import dev.forcecodes.hov.ui.details.DetailsActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GithubUsersFragment : Fragment(R.layout.fragment_github_users) {

    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private val viewModel by viewModels<GithubUserViewModel>()

    private val binding by viewBinding(FragmentGithubUsersBinding::bind)

    private val onClickListener = GithubUsersPagedAdapter.OnClickGithubUserListener { info ->
        startActivity(DetailsActivity.createIntent(requireContext(), info))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagedAdapter = GithubUsersPagedAdapter(onClickListener, viewModel::onLoadMore)

        val loadStateAdapter = LoadStateFooterAdapter(
            viewLifecycleOwner.lifecycleScope,
            viewModel.loadMoreEffect
        )

        binding.recyclerView.adapter = pagedAdapter.withLoadStateAdapter(loadStateAdapter)

        launchWithViewLifecycleScope {
            launch {
                viewModel
                    .pagingFlow
                    .collectLatest(pagedAdapter::submitData)
            }

            launch {
                activityViewModel
                    .refreshState
                    .debounce(DEBOUNCE_DELAY)
                    .collect(::refreshState)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            val snapshotSize = pagedAdapter.snapshot().size
            activityViewModel.onRefresh(snapshotSize)
        }
    }

    private fun refreshState(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }
}

private const val DEBOUNCE_DELAY = 500L
