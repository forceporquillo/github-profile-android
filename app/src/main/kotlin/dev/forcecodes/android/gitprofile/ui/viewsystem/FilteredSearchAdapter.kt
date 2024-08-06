package dev.forcecodes.android.gitprofile.ui.viewsystem

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import dev.forcecodes.android.gitprofile.adapter.AbstractPaginatedViewHolder
import dev.forcecodes.android.gitprofile.adapter.OnClickGithubUserListener
import dev.forcecodes.android.gitprofile.adapter.UserUiModelComparator
import dev.forcecodes.android.gitprofile.binding.viewBinding
import dev.forcecodes.gitprofile.core.model.UserUiModel
import dev.forcecodes.android.gitprofile.databinding.UsersItemLayoutBinding
import dev.forcecodes.android.gitprofile.extensions.executeAfter

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