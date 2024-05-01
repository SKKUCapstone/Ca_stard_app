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
        jsonObject.getString("distance").takeIf { it.isNotEmpty() }?.toDoubleOrNull() ,
        jsonObject.getString("place_url"),
        arrayListOf()
    ) {}
}
