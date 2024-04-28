package edu.skku.map.capstone.models

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
)
