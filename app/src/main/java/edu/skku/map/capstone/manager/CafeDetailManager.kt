package edu.skku.map.capstone.manager
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.cafe.Cafe

class CafeDetailManager private constructor() {

    companion object {
        private var instance: CafeDetailManager? = null

        fun getInstance(): CafeDetailManager {
            if (instance == null) {
                instance = CafeDetailManager()
            }
            return instance!!
        }
    }
    var currentViewingCafe = MutableLiveData<Cafe>()

    fun viewCafe(cafe: Cafe) {
        currentViewingCafe.postValue(cafe)
    }
//    fun unViewCafe() {
//        currentViewingCafe.postValue(null)
//    }

}