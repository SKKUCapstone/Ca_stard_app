package edu.skku.map.capstone.util

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
interface RetrofitService {
    @GET("v2/local/search/category")
    fun fetchCafesKakao(
        @Header("Authorization") restApiKey: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): Call<ResponseBody>

    @GET("v2/local/search/keyword")
    fun fetchCafesByKeywordKakao(
        @Header("Authorization") restApiKey: String,
        @Query("query") keyword: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): Call<ResponseBody>

}