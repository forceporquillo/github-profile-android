package dev.forcecodes.hov.ui.details.repos

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.hov.extensions.launchWithViewLifecycle
import dev.forcecodes.hov.ui.details.BaseDetailsFragment
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RepositoriesFragment : BaseDetailsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RepositoriesAdapter()

        binding.recyclerView.adapter = adapter

        launchWithViewLifecycle {
            viewModel.repositories.collectLatest {
                binding.progressBar.isVisible =
                    it.isLoading && it.data.isEmpty() || !it.error.isNullOrEmpty()
                adapter.submitList(it.data)
            }
        }
    }
}