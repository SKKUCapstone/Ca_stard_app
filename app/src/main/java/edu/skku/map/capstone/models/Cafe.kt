package edu.skku.map.capstone.models

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
//    val powerSocketCnt:Int=0,
//    val capacityCnt:Int=0,
//    val quietCnt:Int=0,
//    val wifiCnt:Int=0,
//    val tablesCnt:Int=0,
//    val toiletCnt:Int=0,
//    val brightCnt:Int=0,
//    val cleanCnt:Int=0,
    val reviews:ArrayList<Review>
){
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getString("id").takeIf { it.isNotEmpty() }?.toLongOrNull() ?: 0L,
        jsonObject.getString("place_name"),
        jsonObject.getString("address_name"),
        jsonObject.getString("road_address_name"),
        jsonObject.getString("phone"),
        jsonObject.getString("y").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        jsonObject.getString("x").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        jsonObject.getString("distance").takeIf { it.isNotEmpty() }?.toDoubleOrNull(),
        jsonObject.getString("place_url"),
//        jsonObject.getInt("capacityCnt"),
//        jsonObject.getInt("powerSocketCnt"),
//        jsonObject.getInt("quietCnt"),
//        jsonObject.getInt("wifiCnt"),
//        jsonObject.getInt("tablesCnt"),
//        jsonObject.getInt("toiletCnt"),
//        jsonObject.getInt("brightCnt"),
//        jsonObject.getInt("cleanCnt"),
        arrayListOf()
    ) {}
}
