package dev.forcecodes.hov.ui.details.orgs

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.forcecodes.hov.adapter.AbstractPaginatedViewHolder
import dev.forcecodes.hov.adapter.PaginatedAdapter
import dev.forcecodes.hov.binding.loadImage
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.databinding.ItemOrganizationLayoutBinding
import dev.forcecodes.hov.domain.usecase.details.OrgsUiModel

class OrganizationsAdapter : ListAdapter<OrgsUiModel,
        OrganizationsAdapter.OrganizationsViewHolder>(OrganizationsUiModelDiff()) {

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

    private class OrganizationsUiModelDiff : DiffUtil.ItemCallback<OrgsUiModel>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationsViewHolder {
        val binding = parent.viewBinding(ItemOrganizationLayoutBinding::inflate)
        return OrganizationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrganizationsViewHolder, position: Int) {
        getItem(position).let(holder::bind)
    }
}