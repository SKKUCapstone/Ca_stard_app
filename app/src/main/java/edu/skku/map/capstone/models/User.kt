package edu.skku.map.capstone.models

data class User(
    val id: Long,
    var userName: String,
    val favorite: ArrayList<Favorite>,
    val recentView:ArrayList<Long>

)
