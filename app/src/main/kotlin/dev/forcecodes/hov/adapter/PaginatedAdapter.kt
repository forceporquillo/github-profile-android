package dev.forcecodes.hov.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers

/**
 * Base class for all adapter components that extends [PagingDataAdapter].
 *
 * @param T: The type value to compare by the [DiffUtil].
 * @param U: A class that extends [AbstractPaginatedViewHolder].
 */
abstract class PaginatedAdapter<T : Any, VH : AbstractPaginatedViewHolder<*, T>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(diffCallback, workerDispatcher = Dispatchers.IO) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH = viewHolder(parent)

    override fun onBindViewHolder(
        holder: VH,
        position: Int
    ) {
        getItem(position)?.let(holder::bind)
    }

    abstract fun viewHolder(parent: ViewGroup): VH
}

/**
 * Base class for all PagedAdapter ViewHolder components that extends [RecyclerView.ViewHolder].
 *
 * @param T: The type value object.
 * @param VB: The root which the items will be inflated and set.
 */
abstract class AbstractPaginatedViewHolder<VB : ViewBinding, T : Any>(
    protected val binding: VB
) : RecyclerView.ViewHolder(binding.root), Binder<T> {

    protected var data: T? = null

    init {
        binding.root.setOnClickListener {
            data?.let { onItemClick(it) }
        }
    }

    override fun bind(data: T) {
        this.data = data
    }

    open fun onItemClick(data: T) {
        // Do nothing
    }
}

/**
 * A binder interface which is used by any [RecyclerView.Adapter]
 * to bind item views within [RecyclerView.ViewHolder].
 * @param T: The type value object.
 */
internal interface Binder<in T> {
    fun bind(data: T)
}
