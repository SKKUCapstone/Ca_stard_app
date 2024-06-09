package edu.skku.map.capstone.models.user
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import org.json.JSONObject
import org.json.JSONArray

val DEFAULT_LAT = 37.402005
val DEFAULT_LNG = 127.108621
class User private constructor() {

    companion object {
        private var instance: User? = null
        var id: Long = 0
        lateinit var email: String
        lateinit var username: String
        lateinit var favorite: ArrayList<Cafe>
        lateinit var review: ArrayList<Review>
        var latLng = MutableLiveData(LatLng.from(DEFAULT_LAT, DEFAULT_LNG))

        fun getInstance(jsonObject: JSONObject): User {
            return instance ?: synchronized(this) {
                instance ?: User().also {
                    id = jsonObject.getLong("id")
                    email = jsonObject.getString("email")
                    username = jsonObject.getString("userName")
                    favorite = parseFavorites(jsonObject.getJSONArray("favorites"))
                    review = parseReviews(jsonObject.getJSONArray("reviews"))
                    instance = it
                }
            }
        }

        private fun parseFavorites(jsonArray: JSONArray): ArrayList<Cafe> {
            val favoriteList = ArrayList<Cafe>()
            for (i in 0 until jsonArray.length()) {
                val cafeJson = jsonArray.getJSONObject(i).getJSONObject("cafe")
                val cafe = Cafe(cafeJson)
                favoriteList.add(cafe)
            }
            return favoriteList
        }

        private fun parseReviews(jsonArray: JSONArray): ArrayList<Review> {
            val reviewList = ArrayList<Review>()
            for (i in 0..<jsonArray.length()){
                val reviewJson = jsonArray.getJSONObject(i)
                val review = Review(reviewJson)
                reviewList.add(review)
            }
            return reviewList
        }
    }
}

// 로그인 관련
data class LoginRequest(
    val email: String,
    val username: String
)