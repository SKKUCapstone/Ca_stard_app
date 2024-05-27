package edu.skku.map.capstone.models.user

import edu.skku.map.capstone.models.favorite.Favorite

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")val id: Long,
    @SerializedName("userName")var userName: String,
    @SerializedName("email")var email: String,
    @SerializedName("reviews")val favorite: ArrayList<Favorite>,
)


// 로그인 관련
data class LoginRequest(
    val email: String,
    val username: String
)



