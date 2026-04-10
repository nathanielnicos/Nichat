package id.nns.nico_chat.di

import android.content.Context
import id.nns.nico_chat.utils.NichatRepository
import id.nns.nico_chat.local.NichatDatabase
import id.nns.nico_chat.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): NichatRepository {
        val nichatDatabase = NichatDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return NichatRepository(nichatDatabase, apiService)
    }
}
