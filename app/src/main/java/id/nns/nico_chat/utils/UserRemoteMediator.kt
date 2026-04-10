package id.nns.nico_chat.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.nns.nico_chat.data.entity.RemoteKeyEntity
import id.nns.nico_chat.data.entity.UserEntity
import id.nns.nico_chat.local.NichatDatabase
import id.nns.nico_chat.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val nichatDatabase: NichatDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, UserEntity>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getAllUser().data
            val endOfPaginationReached = responseData.isEmpty()

            nichatDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    nichatDatabase.remoteKeyDao().deleteRemoteKey()
                    nichatDatabase.userDao().deleteAllUser()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeyEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                nichatDatabase.remoteKeyDao().insertAllRemoteKey(keys)
                nichatDatabase.userDao().insertAllUser(
                    UserConverter.responseListToEntityList(responseData)
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.let { id ->
            nichatDatabase.remoteKeyDao().getRemoteKeyById(id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.id?.let { id ->
            nichatDatabase.remoteKeyDao().getRemoteKeyById(id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                nichatDatabase.remoteKeyDao().getRemoteKeyById(id)
            }
        }
    }
}
