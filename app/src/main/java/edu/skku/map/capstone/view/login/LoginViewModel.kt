package edu.skku.map.capstone.view.login

import android.annotation.SuppressLint
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import edu.skku.map.capstone.manager.MyReviewManager
import edu.skku.map.capstone.models.user.LoginRequest
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.RetrofitService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel {

    companion object{
        fun fetchUserData() {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("Login", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    val email = user.kakaoAccount?.email ?: ""
                    val username = user.kakaoAccount?.profile?.nickname ?: ""
                    val baseUrl = "http://43.201.119.249:8080/"
//                    val baseUrl = R.string.base_url.toString()
                    Log.i(
                        "login", "사용자 정보 요청 성공" +
                                "\n이메일: ${email}" +
                                "\n닉네임: ${username}"
                    )
                    Log.d("login", "BASEURL: ${baseUrl}")
                    val retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val service = retrofit.create(RetrofitService::class.java)

                    service
                        .login(
                            body = LoginRequest(
                                email = email,
                                username = username,
                            )
                        )
                        .enqueue(object : Callback<ResponseBody> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    val body = response.body()!!
                                    val jsonObject = JSONObject(body.string())
                                    User.getInstance(jsonObject)
                                    Log.d(
                                        "login",
                                        "ID: ${User.id}, Email: ${User.email}, Username: ${User.username}"                                        ,
                                    )
                                    User.favorites.forEach { cafe ->
                                        Log.d("login", "Cafe ID: ${cafe.cafeId}, Cafe Name: ${cafe.cafeName}, Address: ${cafe.roadAddressName}, Phone: ${cafe.phone}, Latitude: ${cafe.latitude}, Longitude: ${cafe.longitude}")
                                    }

                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.d("login", "failed to login: ${t.localizedMessage}")
                            }
                        })
                }
            }
        }
    }


}