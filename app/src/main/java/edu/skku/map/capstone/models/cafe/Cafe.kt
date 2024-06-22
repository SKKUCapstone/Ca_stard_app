package edu.skku.map.capstone.models.cafe
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.PropertyName
import edu.skku.map.capstone.models.review.Review
import org.json.JSONObject
import kotlin.math.roundToInt

val threshold = 3.5

class KakaoCafe(
    val cafeId:Long,
    val cafeName:String,
    val roadAddressName:String? = null,
    val phone:String? = null,
    val latitude:Double,
    val longitude:Double,
    val placeURL:String? = null
){
    constructor(jsonObject: JSONObject): this(
        cafeId = jsonObject.getLong("id"),
        cafeName = jsonObject.getString("place_name"),
        roadAddressName = jsonObject.getString("road_address_name"),
        phone = jsonObject.getString("phone"),
        latitude = jsonObject.getDouble("y"),
        longitude = jsonObject.getDouble("x"),
        placeURL = jsonObject.getString("place_url"),
    )
}
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
    @PropertyName("favorite") var isFavorite: Boolean = false
) {

    constructor(doc: DocumentSnapshot): this(
        cafeId = doc.get("cafeId") as Long,
        cafeName = doc.getString("cafeName") as String,
        roadAddressName = doc.getString("roadAddressName") as String,
        phone = doc.getString("phone") as String,
        latitude = doc.getDouble("latitude") as Double,
        longitude = doc.get("longitude") as Double,
        placeURL = doc.get("placeURL") as String,
        capacity = doc.get("capacity") as Double,
        powerSocket = doc.get("powerSocket") as Double,
        quiet = doc.get("quiet") as Double,
        wifi = doc.get("wifi") as Double,
        tables = doc.get("tables") as Double,
        toilet = doc.get("toilet") as Double,
        bright = doc.get("bright") as Double,
        clean = doc.get("clean") as Double,
        capacityCnt = (doc.get("capacityCnt") as Long).toInt(),
        powerSocketCnt = (doc.get("powerSocketCnt") as Long).toInt(),
        quietCnt = (doc.get("quietCnt") as Long).toInt(),
        wifiCnt = (doc.get("wifiCnt") as Long).toInt(),
        tablesCnt = (doc.get("tablesCnt") as Long).toInt(),
        toiletCnt = (doc.get("toiletCnt") as Long).toInt(),
        brightCnt = (doc.get("brightCnt") as Long).toInt(),
        cleanCnt = (doc.get("cleanCnt") as Long).toInt(),
        reviews = arrayListOf(),  // 리뷰가 없으면 빈 리스트
        isFavorite = doc.get("favorite") as Boolean  // isFavorite가 없으면 false
    ) {
        val reviewArray = doc.get("reviews") as ArrayList<Map<String, Any>>
        for(reviewMap in reviewArray) {
            this.reviews.add(Review(reviewMap))
        }
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

    constructor(kakaoCafe: KakaoCafe): this(
        cafeId = kakaoCafe.cafeId,
        cafeName = kakaoCafe.cafeName,
        roadAddressName = kakaoCafe.roadAddressName,
        phone = kakaoCafe.phone,
        latitude = kakaoCafe.latitude,
        longitude = kakaoCafe.longitude,
        placeURL = kakaoCafe.placeURL,
        powerSocket = 0.0,
        capacity = 0.0,
        quiet = 0.0,
        wifi = 0.0,
        tables = 0.0,
        toilet = 0.0,
        bright = 0.0,
        clean = 0.0,
        powerSocketCnt = 0,
        capacityCnt = 0,
        quietCnt = 0,
        wifiCnt = 0,
        tablesCnt = 0,
        toiletCnt = 0,
        brightCnt = 0,
        cleanCnt = 0,
        reviews = arrayListOf(), // Default empty list
        isFavorite = false // Default value
    )

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
            Log.d("review", "  - Review ID: ${review.reviewId}, User Name: ${review.userName} Comment: ${review.comment ?: "No Comment"}")
        }
        Log.d("review", "Is Favorite: ${isFavorite}")
    }

    //update average ratings, counts for each categories and the overall rating with new review
    fun updateRating(review: Review) {

        if (review.quiet > 0) {
            val prev = this.quiet
            val prevc = this.quietCnt
            this.quiet = (prev * prevc + review.quiet) / (prevc + 1)
            this.quietCnt++
        }

        if (review.toilet > 0) {
            val prev = this.toilet
            val prevc = this.toiletCnt
            this.toilet = (prev * prevc + review.toilet) / (prevc + 1)
            this.toiletCnt++
        }

        if (review.powerSocket > 0) {
            val prev = this.powerSocket
            val prevc = this.powerSocketCnt
            this.powerSocket = (prev * prevc + review.powerSocket) / (prevc + 1)
            this.powerSocketCnt++
        }

        if (review.clean > 0) {
            val prev = this.clean
            val prevc = this.cleanCnt
            this.clean = (prev * prevc + review.clean) / (prevc + 1)
            this.cleanCnt++
        }

        if (review.wifi > 0) {
            val prev = this.wifi
            val prevc = this.wifiCnt
            this.wifi = (prev * prevc + review.wifi) / (prevc + 1)
            this.wifiCnt++
        }

        if (review.capacity > 0) {
            val prev = this.capacity
            val prevc = this.capacityCnt
            this.capacity = (prev * prevc + review.capacity) / (prevc + 1)
            this.capacityCnt++
        }

        if (review.bright > 0) {
            val prev = this.bright
            val prevc = this.brightCnt
            this.bright = (prev * prevc + review.bright) / (prevc + 1)
            this.brightCnt++
        }

        if (review.tables > 0) {
            val prev = this.tables
            val prevc = this.tablesCnt
            this.tables = (prev * prevc + review.tables) / (prevc + 1)
            this.tablesCnt++
        }
    }


}
