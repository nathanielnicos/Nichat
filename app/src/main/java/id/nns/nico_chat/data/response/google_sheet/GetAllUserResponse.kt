package id.nns.nico_chat.data.response.google_sheet

data class GetAllUserResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<UserResponse>
)
