package edu.skku.map.capstone.models.review

data class Review(
    val cafeId:Long,
    val userId:Long,
    val cafeName:String,
    val address:String,
    val phone: String,
    val longitude: Double,
    val latitude: Double,
    val powerSocket:Int=0,
    val capacity:Int=0,
    val quiet:Int=0,
    val wifi:Int=0,
    val tables:Int=0,
    val toilet:Int=0,
    val bright:Int=0,
    val clean:Int=0,
    val comment:String?,
    val total:Double=0.0,
)
