package edu.skku.map.capstone.models

data class Favorite(
    var categoryName:String,
    val cafeList:ArrayList<Long>, //array of cafeId
    var icon:String
)
