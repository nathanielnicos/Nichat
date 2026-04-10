package id.nns.nico_chat.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.nns.nico_chat.data.entity.ChatEntity
import id.nns.nico_chat.data.entity.RemoteKeyEntity
import id.nns.nico_chat.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, ChatEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NichatDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var INSTANCE: NichatDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NichatDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NichatDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
