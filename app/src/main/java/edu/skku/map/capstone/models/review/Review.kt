package edu.skku.map.capstone.models.review
import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId

class Review(
    val reviewId:Long,
    var userName:String,
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
    val timeStamp:LocalDateTime,
    val comment:String?
){

    constructor(map: Map<String, Any>) : this(
        reviewId = map["reviewId"] as? Long ?: 0L,
        userName = map["userName"] as? String ?: "",
        cafeId = map["cafeId"] as? Long ?: 0L,
        cafeName = map["cafeName"] as? String ?: "",
        powerSocket = (map["powerSocket"] as? Int) ?: 0,
        capacity = (map["capacity"] as? Int) ?: 0,
        quiet = (map["quiet"] as? Int) ?: 0,
        wifi = (map["wifi"] as? Int) ?: 0,
        tables = (map["tables"] as? Int) ?: 0,
        toilet = (map["toilet"] as? Int) ?: 0,
        bright = (map["bright"] as? Int) ?: 0,
        clean = (map["clean"] as? Int) ?: 0,
        timeStamp = (map["timeStamp"] as? Timestamp)?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: LocalDateTime.now(),
        comment = map["comment"] as? String
    )

    constructor(doc: DocumentSnapshot): this(
        reviewId = doc.get("reviewId") as Long,
        userName = doc.getString("userName") ?: "",
        cafeId = doc.get("cafeId") as Long,
        cafeName = doc.getString("cafeName") ?: "",
        powerSocket = (doc.get("powerSocket") as? Long)?.toInt() ?: 0,
        capacity = (doc.get("capacity") as? Long)?.toInt() ?: 0,
        quiet = (doc.get("quiet") as? Long)?.toInt() ?: 0,
        wifi = (doc.get("wifi") as? Long)?.toInt() ?: 0,
        tables = (doc.get("tables") as? Long)?.toInt() ?: 0,
        toilet = (doc.get("toilet") as? Long)?.toInt() ?: 0,
        bright = (doc.get("bright") as? Long)?.toInt() ?: 0,
        clean = (doc.get("clean") as? Long)?.toInt() ?: 0,
        timeStamp = doc.getTimestamp("timeStamp")?.toDate()?.toInstant()?.atZone(java.time.ZoneId.systemDefault())?.toLocalDateTime() ?: LocalDateTime.now(),
        comment = doc.getString("comment")
    )

    fun abstractReview():ArrayList<Pair<String,Int>> {
        val review = this
        val pairs = arrayListOf<Pair<String,Int>>()
        if(review.capacity != 0) {
            pairs.add(Pair("capacity", review.capacity))
        }
        if(review.quiet != 0) {
            pairs.add(Pair("quiet", review.quiet))
        }
        if(review.toilet != 0) {
            pairs.add(Pair("toilet", review.toilet))
        }
        if(review.wifi != 0) {
            pairs.add(Pair("wiif", review.wifi))
        }
        if(review.bright != 0) {
            pairs.add(Pair("bright", review.bright))
        }
        if(review.clean != 0) {
            pairs.add(Pair("clean", review.clean))
        }
        if(review.powerSocket != 0) {
            pairs.add(Pair("powerSocket", review.powerSocket))
        }
        if(review.tables != 0) {
            pairs.add(Pair("tables", review.tables))
        }
        return pairs
    }
}
