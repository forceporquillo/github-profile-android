package dev.forcecodes.hov.ui.details.orgs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.hov.extensions.launchWithViewLifecycle
import dev.forcecodes.hov.ui.details.BaseDetailsFragment
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OrganizationsFragment : BaseDetailsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OrganizationsAdapter()

        binding.recyclerView.adapter = adapter
        binding.progressBar.isVisible = false

        launchWithViewLifecycle {
            viewModel.organizations.collectLatest(adapter::submitList)
        }
    }
}