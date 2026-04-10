package id.nns.nico_chat.utils

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import id.nns.nico_chat.data.entity.UserEntity
import id.nns.nico_chat.local.NichatDatabase
import id.nns.nico_chat.remote.ApiService

class NichatRepository(
    private val nichatDatabase: NichatDatabase,
    private val apiService: ApiService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getAllUser(): LiveData<PagingData<UserEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = UserRemoteMediator(nichatDatabase, apiService),
            pagingSourceFactory = {
                nichatDatabase.userDao().getAllUser()
            }
        ).liveData
    }
}
