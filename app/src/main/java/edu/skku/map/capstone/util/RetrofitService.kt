package edu.skku.map.capstone.util

import com.google.gson.annotations.SerializedName
import edu.skku.map.capstone.models.user.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class ReviewDTO(
    @SerializedName("userId") private val userId: Long,
    @SerializedName("cafeId") private val cafeId: Long,
    @SerializedName("cafeName") private val cafeName: String,
    @SerializedName("address") private val address: String,
    @SerializedName("phone") private val phone: String,
    @SerializedName("longitude") private val longitude: Double,
    @SerializedName("latitude") private val latitude: Double,
    @SerializedName("powerSocket") private val powerSocket: Int?,
    @SerializedName("capacity") private val capacity: Int?,
    @SerializedName("quiet") private val quiet: Int?,
    @SerializedName("wifi") private val wifi: Int?,
    @SerializedName("tables") private val tables: Int?,
    @SerializedName("toilet") private val toilet: Int?,
    @SerializedName("bright") private val bright: Int?,
    @SerializedName("clean") private val clean: Int?,
    @SerializedName("comment") private val comment: String?
)

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
        @Query("filter") filter: String?,
        @Query("searchText") searchText: String?,
    ): Call<ResponseBody>

    @POST("/review/post")
    fun postReview(
        @Body body: ReviewDTO
    ):Call<ResponseBody>

    @POST("/user/login")
    fun login(
        @Body body: LoginRequest
    ):Call<ResponseBody>
}