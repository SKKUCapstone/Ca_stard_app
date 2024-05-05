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
    val powerSocket:Double=0.0,
    val capacity:Double=0.0,
    val quiet:Double=0.0,
    val wifi:Double=0.0,
    val tables:Double=0.0,
    val toilet:Double=0.0,
    val bright:Double=0.0,
    val clean:Double=0.0,
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
        3.3,3.2,4.3,3.4,4.2,3.7,4.0,3.3,
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
