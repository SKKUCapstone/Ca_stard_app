package edu.skku.map.capstone.models.user

import edu.skku.map.capstone.models.favorite.Favorite

import com.google.gson.annotations.SerializedName

class UserData {
    companion object {
        var id: String? = null
        var email: String? = null
        var username: String? = null
        var favorite: ArrayList<Favorite>? = null
    }
}



// 로그인 관련
data class LoginRequest(
    val email: String,
    val username: String
)



