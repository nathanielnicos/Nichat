package id.nns.nico_chat.data.response.google_sheet

data class GetUserByIdResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: UserResponse
)
