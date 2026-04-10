package id.nns.nico_chat.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.nns.nico_chat.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUser(users: List<UserEntity>)

    @Query("SELECT * FROM user ORDER BY name ASC")
    fun getAllUser(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM user")
    suspend fun deleteAllUser()
}
