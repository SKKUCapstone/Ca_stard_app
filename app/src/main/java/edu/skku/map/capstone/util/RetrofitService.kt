package edu.skku.map.capstone.util

import com.google.gson.annotations.SerializedName
import edu.skku.map.capstone.models.user.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
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
){

}

class FavoriteDTO(
    @SerializedName("userId") private val userId: Long,
    @SerializedName("cafeId") private val cafeId: Long
)

interface RetrofitService {
    //cafes
    @GET("cafes/list")
    fun getCafes(
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("radius") radius: Int,
        @Query("filter") filter: String?,
        @Query("searchText") searchText: String?,
    ): Call<ResponseBody>

    //reviews
    @GET("/review/user/byuserId")
    fun getUserReviews(
        @Query("userId") userId: Long
    ):Call<ResponseBody>

    @GET("/review/cafe/bycafeId")
    fun getCafeReviews(
        @Query("cafeId") cafeId: Long
    ):Call<ResponseBody>

    @POST("/review/post")
    fun postReview(
        @Body body: ReviewDTO
    ):Call<ResponseBody>

    @DELETE("/review/delete")
    fun deleteReview(
        @Query("userId") userId: Long,
        @Query("reviewId") reviewId: Long
    ):Call<ResponseBody>

    //favorite
    @POST("/favorite/post")
    fun addFavorite(
        @Body body: FavoriteDTO
    ):Call<ResponseBody>


    //user
    @POST("/user/login")
    fun login(
        @Body body: LoginRequest
    ):Call<ResponseBody>
}