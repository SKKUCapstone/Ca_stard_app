package edu.skku.map.capstone.models.user
import edu.skku.map.capstone.models.cafe.Cafe
import org.json.JSONObject
import org.json.JSONArray

class User private constructor() {

    companion object {

        private var instance: User? = null
        lateinit var id: String
        lateinit var email: String
        lateinit var username: String
        lateinit var favorite: ArrayList<Cafe>

        fun getInstance(jsonObject: JSONObject): User {
            return instance ?: synchronized(this) {
                instance ?: User().also {
                    id = jsonObject.getString("id")
                    email = jsonObject.getString("email")
                    username = jsonObject.getString("userName")
                    favorite = parseFavorites(jsonObject.getJSONArray("reviews"))
                    instance = it
                }
            }
        }

        private fun parseFavorites(jsonArray: JSONArray): ArrayList<Cafe> {
            val favoriteList = ArrayList<Cafe>()
            for (i in 0 until jsonArray.length()) {
                val cafeJson = jsonArray.getJSONObject(i)
                val cafe = Cafe(cafeJson)
                favoriteList.add(cafe)
            }
            return favoriteList
        }
    }
}

data class Cafe(
    val id: String,
    val name: String
    // Add other fields as necessary
)




// 로그인 관련
data class LoginRequest(
    val email: String,
    val username: String
)



