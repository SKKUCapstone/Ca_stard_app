package edu.skku.map.capstone.util
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import edu.skku.map.capstone.models.cafe.Cafe
import android.util.Log
import edu.skku.map.capstone.manager.CafeManager
import edu.skku.map.capstone.models.cafe.KakaoCafe
import edu.skku.map.capstone.models.review.Review

class firebaseCRUD {

    companion object {
        private val db = Firebase.firestore
        val cafeRef = db.collection("cafe")

        //Read

        //should listen to kakaoCafeList Change.
        //Selectively fetch existing id from firebase or create empty instance and post to CafeManager.
        fun fetchAllCafes() {
            val kakaoCafes = CafeManager.getInstance().kakaoCafes.value ?: return
            Log.d("@@@firebase", "kakaoCafes: ${kakaoCafes.size} cafes: ${kakaoCafes}")
            val cafeIds = kakaoCafes.map { it.cafeId }
            val filters = CafeManager.getInstance().filterCategory.value as List<String>
            if (cafeIds.isEmpty()) return
            var query = cafeRef.whereIn("cafeId", cafeIds)

            // Apply filters if any
            filters.forEach { filter ->
                query = query.whereGreaterThan(filter, 3.5)
            }

            val cafeList = ArrayList<Cafe>()
            query
                .get()
                .addOnSuccessListener { querySnapshot ->
                    kakaoCafes.forEach { kakaoCafe ->
                        val matchingDocument = querySnapshot.documents.find { doc ->
                            doc.getLong("cafeId") == kakaoCafe.cafeId
                        }
                        Log.d("@@@firebase", "kakaoCafe: $kakaoCafe")
                        if (matchingDocument != null) {
                            val cafe = Cafe(matchingDocument)
                            Log.d("@@@firebase", "found matching cafe: $cafe")
                            cafeList.add(cafe)
                        } else {
                            val cafe = Cafe(kakaoCafe)
                            Log.d("@@@firebase", "unmatching cafe: $cafe")
                            cafeList.add(cafe)
                        }
                    }
                    Log.d("@@@firebase","total ${cafeList.size} cafes fetched: ${cafeList}")
                    CafeManager.getInstance().cafes.postValue(cafeList)
                }
                .addOnFailureListener { exception ->
                    Log.d("@@@firebase", "fetchAllCafe failed: $exception")
                }
        }


        //Update
        fun addFavorite() {

        }

        fun deleteFavorite() {

        }

        fun postReview(cafe: Cafe, review: Review) {
            val cafeRef = cafeRef.document(cafe.cafeId.toString())
            cafeRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Cafe exists, update with new review
                        val existingCafe = document.toObject(Cafe::class.java)
                        Log.d("postReview","existingCafe: $existingCafe")

                        existingCafe!!.reviews.add(review)
                        //update Cafe status
                        existingCafe.updateRating(review)

                        // Update the cafe in Firebase
                        cafeRef.set(existingCafe!!)
                            .addOnSuccessListener {
                                Log.d("@@@firebase", "Review added to existing cafe: ${cafe.cafeId}")
                            }
                            .addOnFailureListener { e ->
                                Log.d("@@@firebase", "Error adding review to existing cafe: ", e)
                            }
                    } else {
                        // Cafe doesn't exist, create new cafe data with new review
                        cafe.reviews.add(review)
                        cafe.updateRating(review)

                        // Add the new cafe to Firebase
                        cafeRef.set(cafe)
                            .addOnSuccessListener {
                                Log.d("@@@firebase", "New cafe created with review: ${cafe.cafeId}")
                            }
                            .addOnFailureListener { e ->
                                Log.d("@@@firebase", "Error creating new cafe with review: ", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("@@@firebase", "Error fetching cafe data: ", e)
                }
        }

        fun deleteReview() {

        }
    }

}