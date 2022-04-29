package dev.forcecodes.hov.ui.details

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dev.forcecodes.hov.R
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.databinding.FragmentTabDelegateBinding

abstract class BaseDetailsFragment : Fragment(R.layout.fragment_tab_delegate) {

    protected val viewModel by activityViewModels<DetailsSubViewModel>()
    protected val binding by viewBinding(FragmentTabDelegateBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTabDelegateBinding.bind(view)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }
}