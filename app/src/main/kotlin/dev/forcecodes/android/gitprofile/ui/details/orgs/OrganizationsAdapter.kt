package dev.forcecodes.android.gitprofile.ui.details.orgs

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.forcecodes.android.gitprofile.adapter.AbstractPaginatedViewHolder
import dev.forcecodes.android.gitprofile.binding.loadImage
import dev.forcecodes.android.gitprofile.binding.viewBinding
import dev.forcecodes.android.gitprofile.databinding.ItemOrganizationLayoutBinding
import dev.forcecodes.gitprofile.domain.usecase.details.OrgsUiModel

class OrganizationsAdapter : ListAdapter<OrgsUiModel,
        OrganizationsAdapter.OrganizationsViewHolder>(OrganizationsUiModelDiff) {

    companion object {

        private val OrganizationsUiModelDiff = object: DiffUtil.ItemCallback<OrgsUiModel>() {
            override fun areItemsTheSame(oldItem: OrgsUiModel, newItem: OrgsUiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrgsUiModel,
                newItem: OrgsUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

    class OrganizationsViewHolder(
        binding: ItemOrganizationLayoutBinding
    ) : AbstractPaginatedViewHolder<ItemOrganizationLayoutBinding, OrgsUiModel>(binding) {

        override fun bind(data: OrgsUiModel) {
            binding.apply {
                orgDisplayname.text = data.name
                orgName.text = data.name
                orgBio.text = data.description
                orgAvatar.loadImage(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationsViewHolder {
        val binding = parent.viewBinding(ItemOrganizationLayoutBinding::inflate)
        return OrganizationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrganizationsViewHolder, position: Int) {
        getItem(position).let(holder::bind)
    }
}