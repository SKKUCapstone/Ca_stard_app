package edu.skku.map.capstone.manager
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.util.firebaseCRUD
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MyReviewManager private constructor() {

    companion object {
        private var instance: MyReviewManager? = null
        fun getInstance(): MyReviewManager {
            if (instance == null) {
                instance = MyReviewManager()
            }
            return instance!!
        }
    }

    var reviews = MutableLiveData<ArrayList<Review>>()
    var newReview: Review? = null

    //from userId get ArrayList<Review> and post it to reviews
    fun fetchUserReview() {

    }

    //submit review (callback: fetchUserReview)
    fun onSubmitReview(
        cafe: Cafe,
        capacity: Int?,
        bright: Int?,
        clean: Int?,
        wifi: Int?,
        quiet: Int?,
        tables: Int?,
        powerSocket: Int?,
        toilet: Int?,
        comment: String?
    ) {
        val timeStamp = LocalDateTime.now()
        firebaseCRUD.postReview(cafe, Review(
            reviewId = generateReviewId(timeStamp),
            userName = "정환",
            cafeId = cafe.cafeId,
            cafeName = cafe.cafeName,
            capacity = capacity?: 0,
            bright = bright?: 0,
            clean = clean?: 0,
            wifi = wifi?: 0,
            quiet = quiet?: 0,
            tables = tables?: 0,
            powerSocket = powerSocket?: 0,
            toilet = toilet?: 0,
            comment = comment,
            timeStamp = timeStamp
        ))
    }

    private fun generateReviewId(timestamp: LocalDateTime): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val formattedDateTime = timestamp.format(formatter)
        return formattedDateTime.toLong()
    }

    //get userId and reviewId and delete review (callback: fetchUserReview)
    fun onDeleteReview(reviewId:Long) {

    }




    fun getAllReviews() {
//          reviews.postValue(reviewList)
        //TODO
    }

}