package id.nns.nico_chat.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = "https://script.google.com/macros/s/AKfycbx_ohhkhDO5_8joOkD6yeBJjqyWlgSszSPXKLCo6H5z3xlw8eF8tJERs8gw_ud1sE3Fgw/"

        fun getApiService() : ApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
