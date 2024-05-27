package edu.skku.map.capstone.models.favorite

import edu.skku.map.capstone.models.cafe.Cafe

data class Favorite(
    var categoryName:String,
    val cafeList:ArrayList<Cafe>,
)
