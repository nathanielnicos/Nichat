package id.nns.nico_chat.remote

import id.nns.nico_chat.data.response.google_sheet.GetAllUserResponse
import id.nns.nico_chat.data.response.google_sheet.GetUserByIdResponse
import id.nns.nico_chat.data.response.google_sheet.PostResponse
import id.nns.nico_chat.data.response.google_sheet.UserIdAndStatus
import id.nns.nico_chat.data.response.google_sheet.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("exec?type=all-user")
    suspend fun getAllUser() : GetAllUserResponse

    @GET("exec?type=user-by-id")
    fun getUserById(
        @Query("id")
        id: String
    ) : Call<GetUserByIdResponse>

    @POST("exec?type=new-user")
    fun postNewUser(
        @Body
        user: UserResponse
    ): Call<PostResponse>

    @POST("exec?type=user-status")
    fun postUserStatus(
        @Body
        idAndStatus: UserIdAndStatus
    ): Call<PostResponse>
}
