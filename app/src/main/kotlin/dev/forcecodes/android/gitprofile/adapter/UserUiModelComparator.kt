package dev.forcecodes.android.gitprofile.adapter

import androidx.recyclerview.widget.DiffUtil
import dev.forcecodes.gitprofile.core.model.UserUiModel

val UserUiModelComparator = object : DiffUtil.ItemCallback<UserUiModel>() {

    override fun areItemsTheSame(
        oldItem: UserUiModel,
        newItem: UserUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UserUiModel,
        newItem: UserUiModel
    ): Boolean {
        return oldItem == newItem
    }

}
