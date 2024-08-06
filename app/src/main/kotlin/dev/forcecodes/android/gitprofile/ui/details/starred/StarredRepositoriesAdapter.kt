package dev.forcecodes.android.gitprofile.ui.details.starred

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.forcecodes.android.gitprofile.adapter.AbstractPaginatedViewHolder
import dev.forcecodes.android.gitprofile.binding.loadImage
import dev.forcecodes.android.gitprofile.binding.viewBinding
import dev.forcecodes.android.gitprofile.databinding.ItemStarredLayoutBinding
import dev.forcecodes.gitprofile.domain.usecase.details.StarredUiModel

class StarredRepositoriesAdapter : ListAdapter<StarredUiModel,
        StarredRepositoriesAdapter.StarredRepositoriesViewHolder>(StarredReposUiModelDiff()) {

    class StarredRepositoriesViewHolder(
        binding: ItemStarredLayoutBinding
    ) : AbstractPaginatedViewHolder<ItemStarredLayoutBinding, StarredUiModel>(binding) {

        override fun bind(data: StarredUiModel) {
            binding.apply {
                orgAvatar.loadImage(data.id)
                repoName.text = data.repoName
                ownerName.text = data.name
                stargazer.text = data.starredCount
                repoDescription.text = data.description
                language.text = data.language


                imageColorIcon.isVisible = !data.color.isNullOrEmpty()

                try {
                    imageColorIcon.setColorFilter(Color.parseColor(data.color))
                } catch (e: Exception) { }
            }
        }
    }

    private class StarredReposUiModelDiff : DiffUtil.ItemCallback<StarredUiModel>() {

        override fun areItemsTheSame(
            oldItem: StarredUiModel,
            newItem: StarredUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StarredUiModel,
            newItem: StarredUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StarredRepositoriesViewHolder {
        val binding = parent.viewBinding(ItemStarredLayoutBinding::inflate)
        return StarredRepositoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StarredRepositoriesViewHolder, position: Int) {
        getItem(position).let(holder::bind)
    }
}