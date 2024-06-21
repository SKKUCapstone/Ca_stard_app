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
    val cafeName:String,
    val roadAddressName:String? = null,
    val phone:String? = null,
    val latitude:Double,
    val longitude:Double,
    val placeURL:String? = null,
    var powerSocket:Double = 0.0,
    var capacity:Double = 0.0,
    var quiet:Double = 0.0,
    var wifi:Double = 0.0,
    var tables:Double = 0.0,
    var toilet:Double = 0.0,
    var bright:Double = 0.0,
    var clean:Double = 0.0,
    var powerSocketCnt:Int = 0,
    var capacityCnt:Int = 0,
    var quietCnt:Int = 0,
    var wifiCnt:Int = 0,
    var tablesCnt:Int = 0,
    var toiletCnt:Int = 0,
    var brightCnt:Int = 0,
    var cleanCnt:Int = 0,
    val reviews:ArrayList<Review> = arrayListOf(),
    var isFavorite: Boolean = false
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
        isFavorite = User.getInstance().favorites.any { it.cafeId == this.cafeId }
    }

    constructor(cafe: Cafe, reviews:ArrayList<Review>) : this(
        cafeId = cafe.cafeId,
        cafeName = cafe.cafeName,
        roadAddressName = cafe.roadAddressName,
        phone = cafe.phone,
        latitude = cafe.latitude,
        longitude = cafe.longitude,
        placeURL = cafe.placeURL,
        reviews = reviews,
        isFavorite = cafe.isFavorite
    ){
        // Initialize sums and counts
        var totalCapacity = 0.0
        var totalPowerSocket = 0.0
        var totalQuiet = 0.0
        var totalWifi = 0.0
        var totalTables = 0.0
        var totalToilet = 0.0
        var totalBright = 0.0
        var totalClean = 0.0

        var countCapacity = 0
        var countPowerSocket = 0
        var countQuiet = 0
        var countWifi = 0
        var countTables = 0
        var countToilet = 0
        var countBright = 0
        var countClean = 0

        // Sum up the ratings and counts
        for (review in reviews) {
            if (review.capacity > 0) {
                totalCapacity += review.capacity
                countCapacity++
            }
            if (review.powerSocket > 0) {
                totalPowerSocket += review.powerSocket
                countPowerSocket++
            }
            if (review.quiet > 0) {
                totalQuiet += review.quiet
                countQuiet++
            }
            if (review.wifi > 0) {
                totalWifi += review.wifi
                countWifi++
            }
            if (review.tables > 0) {
                totalTables += review.tables
                countTables++
            }
            if (review.toilet > 0) {
                totalToilet += review.toilet
                countToilet++
            }
            if (review.bright > 0) {
                totalBright += review.bright
                countBright++
            }
            if (review.clean > 0) {
                totalClean += review.clean
                countClean++
            }
        }

        // Update the averages and counts
        capacity = if (countCapacity > 0) totalCapacity / countCapacity else 0.0
        powerSocket = if (countPowerSocket > 0) totalPowerSocket / countPowerSocket else 0.0
        quiet = if (countQuiet > 0) totalQuiet / countQuiet else 0.0
        wifi = if (countWifi > 0) totalWifi / countWifi else 0.0
        tables = if (countTables > 0) totalTables / countTables else 0.0
        toilet = if (countToilet > 0) totalToilet / countToilet else 0.0
        bright = if (countBright > 0) totalBright / countBright else 0.0
        clean = if (countClean > 0) totalClean / countClean else 0.0

        capacityCnt = countCapacity
        powerSocketCnt = countPowerSocket
        quietCnt = countQuiet
        wifiCnt = countWifi
        tablesCnt = countTables
        toiletCnt = countToilet
        brightCnt = countBright
        cleanCnt = countClean
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
        isFavorite = value
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
            .take(3)
        return  ArrayList(ratings)
    }

    fun printCafeDetails() {
        Log.d("review", "Cafe Details:")
        Log.d("review", "ID: $cafeId")
        Log.d("review", "Name: ${cafeName ?: "N/A"}")
        Log.d("review", "Address: ${roadAddressName ?: "N/A"}")
        Log.d("review", "Phone: ${phone ?: "N/A"}")
        Log.d("review", "Latitude: $latitude")
        Log.d("review", "Longitude: $longitude")
        Log.d("review", "Place URL: ${placeURL ?: "N/A"}")
        Log.d("review", "Ratings:")
        Log.d("review", "  - Power Socket: $powerSocket (Count: $powerSocketCnt)")
        Log.d("review", "  - Capacity: $capacity (Count: $capacityCnt)")
        Log.d("review", "  - Quiet: $quiet (Count: $quietCnt)")
        Log.d("review", "  - Wifi: $wifi (Count: $wifiCnt)")
        Log.d("review", "  - Tables: $tables (Count: $tablesCnt)")
        Log.d("review", "  - Toilet: $toilet (Count: $toiletCnt)")
        Log.d("review", "  - Bright: $bright (Count: $brightCnt)")
        Log.d("review", "  - Clean: $clean (Count: $cleanCnt)")
        Log.d("review", "Reviews: ${reviews.size}")
        reviews.forEach { review ->
            Log.d("review", "  - Review ID: ${review.reviewId}, User ID: ${review.userId}, User Name: ${review.userName} Comment: ${review.comment ?: "No Comment"}")
        }
        Log.d("review", "Is Favorite: ${isFavorite}")
    }

}
