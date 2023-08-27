package dev.forcecodes.hov.ui.viewsystem

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import dev.forcecodes.hov.adapter.AbstractPaginatedViewHolder
import dev.forcecodes.hov.adapter.OnClickGithubUserListener
import dev.forcecodes.hov.adapter.UserUiModelComparator
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.core.model.UserUiModel
import dev.forcecodes.hov.databinding.UsersItemLayoutBinding
import dev.forcecodes.hov.extensions.executeAfter

internal class FilteredSearchAdapter(
    private val listener: OnClickGithubUserListener
) : ListAdapter<UserUiModel, FilteredSearchAdapter.FilteredSearchViewHolder>(UserUiModelComparator) {

    internal inner class FilteredSearchViewHolder(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredSearchViewHolder {
        val binding = parent.viewBinding(UsersItemLayoutBinding::inflate)
        return FilteredSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilteredSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}