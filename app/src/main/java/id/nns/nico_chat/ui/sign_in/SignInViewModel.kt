package id.nns.nico_chat.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.nns.nico_chat.data.response.google_sheet.GetUserByIdResponse
import id.nns.nico_chat.data.response.google_sheet.PostResponse
import id.nns.nico_chat.data.response.google_sheet.UserIdAndStatus
import id.nns.nico_chat.data.response.google_sheet.UserResponse
import id.nns.nico_chat.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> get() = _userResponse

    private val _postResponse = MutableLiveData<PostResponse>()
    val postResponse: LiveData<PostResponse> get() = _postResponse

    fun checkUserSheet(checkedUser: UserResponse) {
        ApiConfig.getApiService().getUserById(checkedUser.id)
            .enqueue(object : Callback<GetUserByIdResponse> {
                override fun onResponse(
                    call: Call<GetUserByIdResponse>,
                    response: Response<GetUserByIdResponse>
                ) {
                    if (response.body()?.message == "User not found!") {
                        postNewUser(checkedUser)
                    } else {
                        response.body()?.data?.let { data ->
                            postUserStatusToActive(data)
                        }
                    }
                }

                override fun onFailure(call: Call<GetUserByIdResponse>, t: Throwable) {
                    _postResponse.value = PostResponse(
                        isSuccess = false,
                        message = t.message ?: "Request Failed"
                    )
                }
            })
    }

    private fun postNewUser(newUser: UserResponse) {
        ApiConfig.getApiService().postNewUser(newUser).enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                _userResponse.value = newUser
                _postResponse.value = response.body()
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _postResponse.value = PostResponse(
                    isSuccess = false,
                    message = t.message ?: "Request Failed"
                )
            }
        })
    }

    private fun postUserStatusToActive(activeUser: UserResponse) {
        ApiConfig.getApiService().postUserStatus(
            UserIdAndStatus(
                id = activeUser.id,
                isActive = true
            )
        ).enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                _userResponse.value = activeUser
                _postResponse.value = response.body()
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _postResponse.value = PostResponse(
                    isSuccess = false,
                    message = t.message ?: "Request Failed"
                )
            }
        })
    }
}
