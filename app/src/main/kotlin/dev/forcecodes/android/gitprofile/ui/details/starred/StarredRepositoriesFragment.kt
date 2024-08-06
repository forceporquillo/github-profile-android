package dev.forcecodes.android.gitprofile.ui.details.starred

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.android.gitprofile.extensions.launchWithViewLifecycle
import dev.forcecodes.android.gitprofile.ui.details.BaseDetailsFragment

@AndroidEntryPoint
class StarredRepositoriesFragment : BaseDetailsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StarredRepositoriesAdapter()
        binding.recyclerView.adapter = adapter
        binding.progressBar.isVisible = false

        launchWithViewLifecycle {
            viewModel.starredRepos.collect(adapter::submitList)
        }
    }
}