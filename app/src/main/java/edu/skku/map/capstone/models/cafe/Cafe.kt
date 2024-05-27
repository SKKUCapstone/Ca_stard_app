package edu.skku.map.capstone.models.cafe

import edu.skku.map.capstone.models.review.Review
import org.json.JSONObject

data class Cafe(
    val cafeId:Long,
    val cafeName:String? = null,
    val address:String? = null,
    val roadAddressName:String? = null,
    val phone:String? = null,
    val latitude:Double,
    val longitude:Double,
    val distance:Double? = null,
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
    val reviews:ArrayList<Review> = arrayListOf()
){
    constructor(jsonObject: JSONObject) : this(
        cafeId = jsonObject.getString("id").takeIf { it.isNotEmpty() }?.toLongOrNull() ?: 0L,
        cafeName = jsonObject.getString("place_name"),
        address = jsonObject.getString("address_name"),
        roadAddressName = jsonObject.getString("road_address_name"),
        phone = jsonObject.getString("phone"),
        latitude = jsonObject.getString("y").toDouble(),
        longitude = jsonObject.getString("x").toDouble(),
        distance = jsonObject.getString("distance").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        placeURL = jsonObject.getString("place_url"),
//        capacity = jsonObject.getDouble("capacity"),
//        powerSocket = jsonObject.getDouble("powerSocket"),
//        quiet = jsonObject.getDouble("quiet"),
//        wifi = jsonObject.getDouble("wifi"),
//        tables = jsonObject.getDouble("tables"),
//        toilet = jsonObject.getDouble("toilet"),
//        bright = jsonObject.getDouble("bright"),
//        clean = jsonObject.getDouble("clean"),
//        capacityCnt = jsonObject.getInt("capacityCnt"),
//        powerSocketCnt = jsonObject.getInt("powerSocketCnt"),
//        quietCnt = jsonObject.getInt("quietCnt"),
//        wifiCnt = jsonObject.getInt("wifiCnt"),
//        tablesCnt = jsonObject.getInt("tablesCnt"),
//        toiletCnt = jsonObject.getInt("toiletCnt"),
//        brightCnt = jsonObject.getInt("brightCnt"),
//        cleanCnt = jsonObject.getInt("cleanCnt"),
//        reviews = arrayListOf()
    ) {}
}
