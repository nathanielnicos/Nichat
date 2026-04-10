package id.nns.nico_chat.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.nns.nico_chat.data.entity.ChatEntity

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChat(chats: List<ChatEntity>)

    @Query("SELECT * FROM chat ORDER BY timestamp DESC")
    fun getAllChat(): PagingSource<Int, ChatEntity>

    @Query("DELETE FROM chat")
    suspend fun deleteAllChat()
}
