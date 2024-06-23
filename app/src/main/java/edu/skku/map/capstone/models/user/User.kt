package edu.skku.map.capstone.models.user
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.manager.MyReviewManager
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.util.FavoriteDTO
import edu.skku.map.capstone.util.RetrofitService
import okhttp3.ResponseBody
import org.json.JSONObject
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val DEFAULT_LAT = 37.402005
val DEFAULT_LNG = 127.108621
class User private constructor() {
    companion object {
        private var instance: User? = null

        fun initUser(jsonObject: JSONObject) {
            //initialize myReviewManager
            if(instance == null) instance = User()

            val reviews = this.instance!!.parseReviews(jsonObject.getJSONArray("reviews"))
//            Log.d("@@@myreviews",reviews[0].reviewId.toString())
            MyReviewManager.getInstance().reviews.postValue(reviews)
            Log.d("로그인저장", "id: ${jsonObject.getLong("id")}")

            this.instance!!.id = jsonObject.getLong("id")
            this.instance!!.email = jsonObject.getString("email")
            this.instance!!.userName = jsonObject.getString("userName")
            instance!!.favorites.postValue(instance!!.parseFavorites(jsonObject.getJSONArray("favorites")))
            Log.d("login", "User initialized")
            Log.d("login", jsonObject.getJSONArray("favorites").toString())
        }

        fun refresh() {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://43.201.119.249:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(RetrofitService::class.java)

            service
                .getUserInfo(instance!!.id)
                .enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()!!
                            val jsonObject = JSONObject(body.string())
                            instance!!.id = jsonObject.getLong("id")
                            instance!!.email = jsonObject.getString("email")
                            instance!!.userName = jsonObject.getString("userName")
                            instance!!.favorites.postValue(instance!!.parseFavorites(jsonObject.getJSONArray("favorites")))
                        } else {
                            Log.d("user", "error while refreshing, err:${response}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("error", "error while refreshing: ${t.localizedMessage}")
                    }
                })
        }

        fun getInstance():User {
            if(instance == null) {
                instance =  User()
                return instance!!
            }
            return this.instance!!
        }
    }
    var id: Long = 0
    var email = "userEmail"
    var userName = "userName"
    var favorites = MutableLiveData<ArrayList<Cafe>>(arrayListOf())
    var latLng = MutableLiveData(LatLng.from(DEFAULT_LAT, DEFAULT_LNG))

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

// 로그인 관련
data class LoginRequest(
    val email: String,
    val username: String
)