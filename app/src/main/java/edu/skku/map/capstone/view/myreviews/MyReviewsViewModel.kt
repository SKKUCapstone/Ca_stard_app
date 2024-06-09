package edu.skku.map.capstone.view.myreviews

import android.annotation.SuppressLint
import android.util.Log
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.FavoriteDTO
import edu.skku.map.capstone.util.RetrofitService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyReviewsViewModel {
    fun fetchUserReviews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .getReviews(User.id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
//                    val body = response.body()!!
                    Log.d("review", response.body().toString())

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("review", "failed to add favorite: ${t.localizedMessage}")
                }
            })
    }


}