package id.nns.nico_chat.data.response.firebase

data class ChatResponse(
    val id: String,
    val userId: String,
    val partnerId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: String
)
