package edu.skku.map.capstone.models.cafe
import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt

val threshold = 3.5
class Cafe(
    val cafeId:Long,
    val cafeName:String? = null,
    val roadAddressName:String? = null,
    val phone:String? = null,
    val latitude:Double,
    val longitude:Double,
    val placeURL:String? = null,
    val powerSocket:Double = 0.0,
    val capacity:Double = 0.0,
    val quiet:Double = 0.0,
    val wifi:Double = 0.0,
    val tables:Double = 0.0,
    val toilet:Double = 0.0,
    val bright:Double = 0.0,
    val clean:Double = 0.0,
    val powerSocketCnt:Int = 0,
    val capacityCnt:Int = 0,
    val quietCnt:Int = 0,
    val wifiCnt:Int = 0,
    val tablesCnt:Int = 0,
    val toiletCnt:Int = 0,
    val brightCnt:Int = 0,
    val cleanCnt:Int = 0,
    val reviews:ArrayList<Review> = arrayListOf(),
    var isFavorite: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
){
    constructor(jsonObject: JSONObject) : this(
        cafeId = jsonObject.getLong("id"),
        cafeName = jsonObject.getString("cafe_name"),
        roadAddressName = jsonObject.getString("road_address_name"),
        phone = jsonObject.getString("phone"),
        latitude = jsonObject.getString("latitude").toDouble(),
        longitude = jsonObject.getString("longitude").toDouble(),
        placeURL = jsonObject.getString("place_url"),
        capacity = jsonObject.getDouble("capacity"),
        powerSocket = jsonObject.getDouble("power_socket"),
        quiet = jsonObject.getDouble("quiet"),
        wifi = jsonObject.getDouble("wifi"),
        tables = jsonObject.getDouble("tables"),
        toilet = jsonObject.getDouble("toilet"),
        bright = jsonObject.getDouble("bright"),
        clean = jsonObject.getDouble("clean"),
        capacityCnt = jsonObject.getInt("capacity_cnt"),
        powerSocketCnt = jsonObject.getInt("power_socket_cnt"),
        quietCnt = jsonObject.getInt("quiet_cnt"),
        wifiCnt = jsonObject.getInt("wifi_cnt"),
        tablesCnt = jsonObject.getInt("tables_cnt"),
        toiletCnt = jsonObject.getInt("toilet_cnt"),
        brightCnt = jsonObject.getInt("bright_cnt"),
        cleanCnt = jsonObject.getInt("clean_cnt"),
        reviews = jsonObject.optJSONArray("reviews")?.let { parseReview(it) } ?: arrayListOf(),  // 리뷰가 없으면 빈 리스트
    ) {
        this.isFavorite.value = User.getInstance().favorites.any { it.cafeId == this.cafeId }
        Log.d("Cafe", "Cafe created from JSONObject with isFavorite: ${isFavorite.value}")
    }

    companion object {
        fun parseReview(jsonArray: JSONArray):ArrayList<Review> {
            val reviewArray: ArrayList<Review> = arrayListOf()
            for (i in 0 until jsonArray.length()) {
                val reviewJsonObject = jsonArray.getJSONObject(i)
                val review = Review(reviewJsonObject)
                reviewArray.add(review)
            }
            return reviewArray
        }
    }
    
    fun getTotalCnt():Int {
        return capacityCnt + brightCnt + cleanCnt + quietCnt + wifiCnt + tablesCnt + powerSocketCnt + toiletCnt
    }

    private fun getTotalRatingSum():Double {
        return (capacity * capacityCnt
                + bright * brightCnt
                + clean * cleanCnt
                + quiet * quietCnt
                + wifi * wifiCnt
                + tables * tablesCnt
                + powerSocket * powerSocketCnt
                + toilet * toiletCnt)
    }
    fun getTotalRating():Double? {
        return if(getTotalCnt() == 0) null
        else{
            val average = getTotalRatingSum()/getTotalCnt()
            return (average * 10).roundToInt().toDouble() / 10
        }
    }

    fun filterTopReviews():ArrayList<String> {
        val filteredList = arrayListOf<String>()
        var sum = 0
        var size = 0
        for(review in reviews){
            sum += review.bright
            size += if(review.bright == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("bright")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.clean
            size += if(review.clean == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("clean")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.quiet
            size += if(review.quiet == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("quiet")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.capacity
            size += if(review.capacity == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("capacity")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.powerSocket
            size += if(review.powerSocket == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("powerSocket")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.wifi
            size += if(review.wifi == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("wifi")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.tables
            size += if(review.tables == 0) 0 else 1}
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("table")
        sum = 0
        size = 0
        for(review in reviews){
            sum += review.toilet
            size += if(review.toilet == 0) 0 else 1
        }
        if(size != 0 && sum.toDouble()/size.toDouble() > threshold ) filteredList.add("toilet")

        return filteredList
    }

    fun updateIsFavorite(value: Boolean) {
        isFavorite.postValue(value)
    }

    fun getTopCategories(): ArrayList<String> {
        val ratings = mapOf(
            "bright" to bright,
            "clean" to clean,
            "quiet" to quiet,
            "capacity" to capacity,
            "powerSocket" to powerSocket,
            "wifi" to wifi,
            "tables" to tables,
            "toilet" to toilet
        ).filter { it.value > threshold }
            .entries
            .sortedByDescending { it.value }
            .map { it.key }
        return ratings as ArrayList<String>
    }
}
