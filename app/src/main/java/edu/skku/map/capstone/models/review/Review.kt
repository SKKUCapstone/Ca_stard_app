package edu.skku.map.capstone.models.review
import org.json.JSONObject
import java.time.LocalDateTime

data class Cafe_(
    val cafeId: Long,
    val cafeName: String
){
    constructor(jsonObject: JSONObject): this(
        cafeId = jsonObject.getLong("id"),
        cafeName = jsonObject.getString("cafe_name"),
    )
}

data class User_(
    val userId: Long
){
    constructor(jsonObject: JSONObject): this(
        userId = jsonObject.getLong("id")
    )
}

class Review(
    val reviewId:Long,
    val userId:Long,
    val cafeId:Long,
    val cafeName:String,
    val powerSocket:Int=0,
    val capacity:Int=0,
    val quiet:Int=0,
    val wifi:Int=0,
    val tables:Int=0,
    val toilet:Int=0,
    val bright:Int=0,
    val clean:Int=0,
//    val timeStamp:LocalDateTime,
    val comment:String?
){
    constructor(jsonObject: JSONObject): this(
        reviewId=jsonObject.getLong("id"),
        cafeId=Cafe_(jsonObject.getJSONObject("cafe")).cafeId,
        userId=User_(jsonObject.getJSONObject("user")).userId,
        cafeName=Cafe_(jsonObject.getJSONObject("cafe")).cafeName,
        powerSocket= jsonObject.getInt("power_socket"),
        capacity=jsonObject.getInt("capacity"),
        quiet=jsonObject.getInt("quiet"),
        wifi=jsonObject.getInt("wifi"),
        tables=jsonObject.getInt("tables"),
        toilet=jsonObject.getInt("toilet"),
        bright=jsonObject.getInt("bright"),
        clean=jsonObject.getInt("clean"),
//        timeStamp= LocalDateTime(),
        comment=if(jsonObject.isNull("comment") || !jsonObject.isNull("comment")&&jsonObject.getString("comment").trim() == "" ) null
                else jsonObject.getString("comment").trim()
    )

}
