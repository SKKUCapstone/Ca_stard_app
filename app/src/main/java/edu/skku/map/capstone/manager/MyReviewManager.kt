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


    fun getUserReview() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .getUserReviews(User.id)
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

    fun getCafeReview(): ArrayList<Review> {
        //TODO
        return arrayListOf()
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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .postReview(
                body = ReviewDTO(
                    userId = User.id,
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
            )
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("review", response.body().toString())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("review", "failed to post review: ${t.localizedMessage}")
                }
            })
    }

//    fun deleteReview(reviewId: Long) {
//        val newReviews = reviews.value!!.filter {
//            it.reviewId != reviewId
//        }
//        reviews.postValue(newReviews as ArrayList<Review>)
//    }

    fun onDeleteReview(
        userId:Long,
        reviewId:Long,
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .deleteReview(
                userId, reviewId
            )
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("@@@myreview", response.body().toString())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("@@@myreview", "failed to delete review: ${t.localizedMessage}")
                }
            })

    }
}