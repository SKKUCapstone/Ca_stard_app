package edu.skku.map.capstone.manager
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review

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

    }

    //get userId and reviewId and delete review (callback: fetchUserReview)
    fun onDeleteReview(reviewId:Long) {

    }




    fun getAllReviews() {
//          reviews.postValue(reviewList)
        //TODO
    }

}