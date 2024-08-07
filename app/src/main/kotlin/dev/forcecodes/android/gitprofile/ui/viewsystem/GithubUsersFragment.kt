package dev.forcecodes.android.gitprofile.ui.viewsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.android.gitprofile.R
import dev.forcecodes.android.gitprofile.adapter.GithubUsersPagedAdapter
import dev.forcecodes.android.gitprofile.adapter.LoadStateFooterAdapter
import dev.forcecodes.android.gitprofile.adapter.OnClickGithubUserListener
import dev.forcecodes.android.gitprofile.binding.viewBinding
import dev.forcecodes.android.gitprofile.databinding.FragmentGithubUsersBinding
import dev.forcecodes.android.gitprofile.extensions.launchWithViewLifecycleScope
import dev.forcecodes.android.gitprofile.ui.GithubUserUiEvent
import dev.forcecodes.android.gitprofile.ui.GithubUserViewModel
import dev.forcecodes.android.gitprofile.ui.MainActivityViewModel
import dev.forcecodes.android.gitprofile.ui.details.DetailsActivity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GithubUsersFragment : Fragment(R.layout.fragment_github_users) {

    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private val viewModel by viewModels<GithubUserViewModel>()

    private val binding by viewBinding(FragmentGithubUsersBinding::bind)

    private val onClickListener = OnClickGithubUserListener { info ->
        startActivity(DetailsActivity.createIntent(requireContext(), info))
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagedAdapter =
            GithubUsersPagedAdapter(
                onClickListener,
                viewModel::onLoadMore
            )

        val loadStateAdapter = LoadStateFooterAdapter(
            viewLifecycleOwner.lifecycleScope,
            viewModel.loadMoreEffect
        )

        binding.recyclerView.adapter = pagedAdapter.withLoadStateAdapter(loadStateAdapter) {
            activityViewModel.sendEvent(GithubUserUiEvent.OnLoad(1))
        }

        val filteredSearchAdapter = FilteredSearchAdapter(onClickListener)

        with(binding) {
            recyclerView.adapter = pagedAdapter.withLoadStateAdapter(loadStateAdapter) {
                activityViewModel.sendEvent(GithubUserUiEvent.OnLoad(1))
            }

            filteredSearchList.adapter = filteredSearchAdapter

            searchView.apply {
                // do not consume inset inside SearchView#setUpStatusBarSpacerInsetListener
                setStatusBarSpacerEnabled(false)
                editText
                    .doOnTextChanged {
                        viewModel.clearSelections()
                    }
                    .onEach { query ->
                        viewModel.searchUser(query)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            swipeRefresh.setOnRefreshListener {
                pagedAdapter.refresh()
                val snapshotSize = pagedAdapter.snapshot().size
                activityViewModel.onRefresh(snapshotSize)
            }
        }

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

            launch {
                viewModel.userSearchResults.collect {
                    filteredSearchAdapter.submitList(it)
                }
            }

            launch {
                viewModel.showSearchErrorMessage.collect { showError ->
                    binding.searchErrorText.isVisible = showError
                }
            }
        }
    }

    private fun refreshState(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }
}

private const val DEBOUNCE_DELAY = 500L
