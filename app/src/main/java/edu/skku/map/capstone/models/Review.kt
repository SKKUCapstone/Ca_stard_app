package edu.skku.map.capstone.models

data class Review(
    val cafeId:Long,
    val userID:Long,
    val powerSocket:Int=0,
    val capacity:Int=0,
    val quiet:Int=0,
    val wifi:Int=0,
    val table:Int=0,
    val toilet:Int=0,
    val bright:Int=0,
    val clean:Int=0,
    val total:Double=0.0,
)
