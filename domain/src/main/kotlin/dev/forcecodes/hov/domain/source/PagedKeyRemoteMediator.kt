package dev.forcecodes.hov.domain.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.cache.entity.KeyIndex

private const val STARTING_PAGE_INDEX = 1

@Deprecated("unused")
@OptIn(ExperimentalPagingApi::class)
class PagedKeyRemoteMediator<Cache : Any, Remote : Any>(
    private val onSuccess: suspend (page: Int, size: Int) -> List<Remote>,
    private val refreshKeys: suspend (items: List<Remote>, nextKey: Int?, prevKey: Int?, clear: Boolean) -> Unit,
    private val findKeyDelegate: suspend (Cache) -> KeyIndex?,
) : RemoteMediator<Int, Cache>() {

    private var previousKey: Int? = null

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Cache>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextSince?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevSince
                    ?: return MediatorResult.Success(remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                var nextKey = remoteKeys?.nextSince
                    ?: return MediatorResult.Success(remoteKeys != null)

                if (previousKey == null) {
                    previousKey = nextKey
                } else {
                    nextKey = previousKey!!
                    previousKey = nextKey + 1
                }

                nextKey
            }
        }

        return try {
            val result = onSuccess.invoke(page, state.config.pageSize)
            val endOfPaginationReached = result.isEmpty()

            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1

            refreshKeys.invoke(result, nextKey, prevKey, loadType == LoadType.REFRESH)

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Cache>): KeyIndex? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                Logger.e("Last Item: $item")
                // Get the remote keys of the last item retrieved
                findKeyDelegate.invoke(item)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Cache>): KeyIndex? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                // Get the remote keys of the first items retrieved
                findKeyDelegate.invoke(item)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Cache>
    ): KeyIndex? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { item ->
                findKeyDelegate.invoke(item)
            }
        }
    }
}