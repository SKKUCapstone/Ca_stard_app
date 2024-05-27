package edu.skku.map.capstone.models.cafe

import edu.skku.map.capstone.models.review.Review
import org.json.JSONObject

data class Cafe(
    val cafeId:Long,
    val cafeName:String?,
    val address:String?,
    val roadAddressName:String?,
    val phone:String?,
    val latitude:Double?,
    val longitude:Double?,
    val distance:Double?,
    val placeURL:String?,
    val powerSocket:Double,
    val capacity:Double,
    val quiet:Double,
    val wifi:Double,
    val tables:Double,
    val toilet:Double,
    val bright:Double,
    val clean:Double,
    val powerSocketCnt:Int,
    val capacityCnt:Int,
    val quietCnt:Int,
    val wifiCnt:Int,
    val tablesCnt:Int,
    val toiletCnt:Int,
    val brightCnt:Int,
    val cleanCnt:Int,
    val reviews:ArrayList<Review>
){
    constructor(jsonObject: JSONObject) : this(
        cafeId = jsonObject.getString("id").takeIf { it.isNotEmpty() }?.toLongOrNull() ?: 0L,
        cafeName = jsonObject.getString("place_name"),
        address = jsonObject.getString("address_name"),
        roadAddressName = jsonObject.getString("road_address_name"),
        phone = jsonObject.getString("phone"),
        latitude = jsonObject.getString("y").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        longitude = jsonObject.getString("x").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        distance = jsonObject.getString("distance").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        placeURL = jsonObject.getString("place_url"),
        capacity = jsonObject.getDouble("capacity"),
        powerSocket = jsonObject.getDouble("powerSocket"),
        quiet = jsonObject.getDouble("quiet"),
        wifi = jsonObject.getDouble("wifi"),
        tables = jsonObject.getDouble("tables"),
        toilet = jsonObject.getDouble("toilet"),
        bright = jsonObject.getDouble("bright"),
        clean = jsonObject.getDouble("clean"),
        capacityCnt = jsonObject.getInt("capacityCnt"),
        powerSocketCnt = jsonObject.getInt("powerSocketCnt"),
        quietCnt = jsonObject.getInt("quietCnt"),
        wifiCnt = jsonObject.getInt("wifiCnt"),
        tablesCnt = jsonObject.getInt("tablesCnt"),
        toiletCnt = jsonObject.getInt("toiletCnt"),
        brightCnt = jsonObject.getInt("brightCnt"),
        cleanCnt = jsonObject.getInt("cleanCnt"),
        reviews = arrayListOf()
    ) {}
}
