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
    @SerializedName("userId") val userId: Long,
    @SerializedName("cafeId") val cafeId: Long,
    @SerializedName("cafeName") val cafeName: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("powerSocket") val powerSocket: Int?,
    @SerializedName("capacity") val capacity: Int?,
    @SerializedName("quiet") val quiet: Int?,
    @SerializedName("wifi") val wifi: Int?,
    @SerializedName("tables") val tables: Int?,
    @SerializedName("toilet") val toilet: Int?,
    @SerializedName("bright") val bright: Int?,
    @SerializedName("clean") val clean: Int?,
    @SerializedName("comment") val comment: String?
)

class FavoriteDTO(
    @SerializedName("userId") val userId: Long,
    @SerializedName("cafeId") val cafeId: Long
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

    //Recommend Cafes
    @GET("cafes/recommend")
    fun getRecommendCafes(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("userId") userId: Long,
    ): Call<ResponseBody>
    //reviews

    @GET("/review/user")
    fun getUserReviews(
        @Query("userId") userId: Long
    ):Call<ResponseBody>

    //get all cafe reviews by cafeId
    @GET("/review/cafe")
    fun getCafeReviews(
        @Query("cafeId") cafeId: Long
    ):Call<ResponseBody>

    //post review
    @POST("/review/post")
    fun postReview(
        @Body body: ReviewDTO
    ):Call<ResponseBody>

    //delete review
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

    @DELETE("/favorite/delete")
    fun deleteFavorite(
        @Query("userId") userId: Long,
        @Query("cafeId") cafeId: Long
    ): Call<ResponseBody>
    //user
    @POST("/user/login")
    fun login(
        @Body body: LoginRequest
    ):Call<ResponseBody>
}