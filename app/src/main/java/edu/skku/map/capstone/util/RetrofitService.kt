package edu.skku.map.capstone.util

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//interface RetrofitService {
//    @GET("v2/local/search/category")
//    fun getCafes(
//        @Header("Authorization") restApiKey: String,
//        @Query("category_group_code") categoryGroupCode: String,
//        @Query("x") x: String,
//        @Query("y") y: String,
//        @Query("radius") radius: Int
//    ): Call<ResponseBody>
//}

interface RetrofitService {
    @GET("cafes/list")
    fun getCafes(
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int,
        @Query("searchText") searchText: String? = null,
        @Query("powerSocket") powerSocket: Boolean? = null,
        @Query("capacity") capacity: Boolean? = null,
        @Query("quiet")  quiet: Boolean? = null,
        @Query("wifi")  wifi: Boolean? = null,
        @Query("tables")  tables: Boolean? = null,
        @Query("toilet")  toilet: Boolean? = null,
        @Query("bright")  bright: Boolean? = null,
        @Query("clean") clean: Boolean? = null,
    ): Call<ResponseBody>
}