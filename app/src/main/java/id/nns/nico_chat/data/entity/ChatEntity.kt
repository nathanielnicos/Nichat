package id.nns.nico_chat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val partnerId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: String
)
