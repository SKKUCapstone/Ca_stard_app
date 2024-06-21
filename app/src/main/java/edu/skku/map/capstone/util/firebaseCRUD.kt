package edu.skku.map.capstone.util
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import edu.skku.map.capstone.models.cafe.Cafe
import android.util.Log
class firebaseCRUD {

    companion object{
        private val db = Firebase.firestore
        val cafeRef = db.collection("cafe")
    }


    //Create
    fun addCafe(cafe: Cafe) {
        cafeRef
            .add(cafe)
            .addOnSuccessListener{
                Log.d("@@@firebase", "addCafe succeed: $it")
            }
            .addOnFailureListener{
                Log.d("@@@firebase", "addCafe failed: $it")
            }
    }

    //Read
    fun fetchAllCafes():ArrayList<Cafe> {
        val res = cafeRef
            .get()
            .addOnSuccessListener {
                Log.d("@@@firebase", "fetchAllCafe succeed: $it")
            }
            .addOnFailureListener{
                Log.d("@@@firebase", "fetchAllCafe failed: $it")
            }
        return arrayListOf()
    }

    //Update
    fun addFavorite() {

    }

    fun deleteFavorite() {

    }

    fun postReview() {

    }
    fun deleteReview() {

    }

}