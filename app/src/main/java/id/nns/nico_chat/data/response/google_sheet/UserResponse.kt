package id.nns.nico_chat.data.response.google_sheet

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String,
    val isActive: Boolean
)
