package edu.skku.map.capstone.models.user

import edu.skku.map.capstone.models.favorite.Favorite

data class User(
    val id: Long,
    var userName: String,
    val favorite: ArrayList<Favorite>,
    val recentView:ArrayList<Long>
)
