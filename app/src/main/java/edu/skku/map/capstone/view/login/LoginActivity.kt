package edu.skku.map.capstone.view.login

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ActivityLoginBinding
import edu.skku.map.capstone.models.user.LoginRequest
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.util.ReviewDTO
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    // 카카오 로그인
    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.refreshToken}")
            fetchUserData()
            navigateMainActivity()
            finish()
        }
    }
    // 로그인 정보
    lateinit var userEmail: EditText
    lateinit var userPassword: EditText
    lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
//        userEmail = binding.valLoginEmail
//        userPassword = binding.valLoginPassword
//        btnLogin = binding.btnKakaologin

        setActions()
    }

    private fun setActions() {
        binding.btnKakaoLogin.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.refreshToken}")
                        fetchUserData()
                        navigateMainActivity()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
    private fun navigateMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        fun fetchUserData() {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    val email = user.kakaoAccount?.email ?: ""
                    val username = user.kakaoAccount?.profile?.nickname ?: ""
                    val baseUrl = "http://43.201.119.249:8080/"
//                    val baseUrl = R.string.base_url.toString()
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n이메일: ${email}" +
                            "\n닉네임: ${username}")
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
                                if(response.isSuccessful) {
                                    Log.d("loginResponse", "백엔드와 통신완료")
                                    val body = response.body()!!
                                    val jsonObject = JSONObject(body.string())
                                    User.getInstance(jsonObject)
                                    Log.d("loginResponse", "ID: ${User.id}, Email: ${User.email}, Username: ${User.username}")

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