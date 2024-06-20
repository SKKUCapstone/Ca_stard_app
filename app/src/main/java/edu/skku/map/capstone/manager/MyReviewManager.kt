package edu.skku.map.capstone.manager
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.util.ReviewDTO
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyReviewManager private constructor() {

    companion object {
        private var instance: MyReviewManager? = null

        fun getInstance(): MyReviewManager {
            if (instance == null) {
                instance = MyReviewManager()
            }
            return instance!!
        }
    }

    var reviews = MutableLiveData<ArrayList<Review>>()
//    var isReviewChanged = MutableLiveData("") //"write", "delete", ""
    var newReview : Review? = null
//    var deletedReviewId : Long? = null

    fun fetchUserReview() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .getUserReviews(User.getInstance().id)
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val body = response.body()!!
                    val jsonArray = JSONArray(body.string())
                    val reviewList = arrayListOf<Review>()
                    for(i in 0..<jsonArray.length()) {
                        reviewList.add(Review(jsonArray.getJSONObject(i)))
                    }
                    Log.d("@@@review", reviewList.toString())
                    reviews.postValue(reviewList)
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("@@@myreview", "failed to delete review: ${t.localizedMessage}")
                }
            })
    }

    fun onSubmitReview(
        cafe: Cafe,
        capacity: Int?,
        bright: Int?,
        clean: Int?,
        wifi: Int?,
        quiet: Int?,
        tables: Int?,
        powerSocket: Int?,
        toilet: Int?,
        comment: String?
    ) {
        val review = ReviewDTO(
            userId = User.getInstance().id,
            cafeId = cafe.cafeId,
            cafeName = cafe.cafeName ?: "",
            address = cafe.roadAddressName ?: "",
            phone = cafe.phone ?: "",
            latitude = cafe.latitude,
            longitude = cafe.longitude,
            capacity = capacity,
            bright = bright,
            quiet = quiet,
            wifi = wifi,
            tables = tables,
            toilet = toilet,
            clean = clean,
            powerSocket = powerSocket,
            comment = comment
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .postReview(
                body = review
            )
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    fetchUserReview()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("review", "failed to post review: ${t.localizedMessage}")
                }
            })
    }

    fun onDeleteReview(
        reviewId:Long,
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .deleteReview(User.getInstance().id, reviewId)
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    fetchUserReview()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("@@@delete", "failed to delete review: ${t.localizedMessage}")
                }
            })

    }
}