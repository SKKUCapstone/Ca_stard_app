package edu.skku.map.capstone

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("v2/local/search/category")
    fun getCafes(
        @Header("Authorization") restApiKey: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): Call<ResponseBody>

}