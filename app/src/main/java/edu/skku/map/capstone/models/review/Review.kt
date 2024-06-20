package edu.skku.map.capstone.models.review
import android.annotation.SuppressLint
import android.util.Log
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.util.ReviewDTO
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

data class Cafe_(
    val cafeId: Long,
    val cafeName: String
){
    constructor(jsonObject: JSONObject): this(
        cafeId = jsonObject.getLong("id"),
        cafeName = jsonObject.getString("cafe_name"),
    )
}

data class User_(
    val userId: Long,
    val userName: String
){
    constructor(jsonObject: JSONObject): this(
        userId = jsonObject.getLong("id"),
        userName = jsonObject.getString("userName")
    )
}

class Review(
    val reviewId:Long,
    val userId:Long,
    var userName:String,
    val cafeId:Long,
    val cafeName:String,
    val powerSocket:Int=0,
    val capacity:Int=0,
    val quiet:Int=0,
    val wifi:Int=0,
    val tables:Int=0,
    val toilet:Int=0,
    val bright:Int=0,
    val clean:Int=0,
//    val timeStamp:LocalDateTime,
    val comment:String?
){
    constructor(jsonObject: JSONObject): this(
        reviewId=jsonObject.getLong("id"),
        cafeId=Cafe_(jsonObject.getJSONObject("cafe")).cafeId,
        userId=User_(jsonObject.getJSONObject("user")).userId,
        userName=User_(jsonObject.getJSONObject("user")).userName,
        cafeName=Cafe_(jsonObject.getJSONObject("cafe")).cafeName,
        powerSocket= jsonObject.getInt("power_socket"),
        capacity=jsonObject.getInt("capacity"),
        quiet=jsonObject.getInt("quiet"),
        wifi=jsonObject.getInt("wifi"),
        tables=jsonObject.getInt("tables"),
        toilet=jsonObject.getInt("toilet"),
        bright=jsonObject.getInt("bright"),
        clean=jsonObject.getInt("clean"),
//        timeStamp= LocalDateTime(),
        comment=if(jsonObject.isNull("comment") || !jsonObject.isNull("comment")&&jsonObject.getString("comment").trim() == "" ) null
                else jsonObject.getString("comment").trim()
    ){}

    fun abstractReview():ArrayList<Pair<String,Int>> {
        val review = this
        val pairs = arrayListOf<Pair<String,Int>>()
        if(review.capacity != 0) {
            pairs.add(Pair("capacity", review.capacity))
        }
        if(review.quiet != 0) {
            pairs.add(Pair("quiet", review.quiet))
        }
        if(review.toilet != 0) {
            pairs.add(Pair("toilet", review.toilet))
        }
        if(review.wifi != 0) {
            pairs.add(Pair("wiif", review.wifi))
        }
        if(review.bright != 0) {
            pairs.add(Pair("bright", review.bright))
        }
        if(review.clean != 0) {
            pairs.add(Pair("clean", review.clean))
        }
        if(review.powerSocket != 0) {
            pairs.add(Pair("powerSocket", review.powerSocket))
        }
        if(review.tables != 0) {
            pairs.add(Pair("tables", review.tables))
        }
        return pairs
    }
}
