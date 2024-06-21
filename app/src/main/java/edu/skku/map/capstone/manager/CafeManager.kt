package edu.skku.map.capstone.manager
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.cafe.Cafe

class CafeManager private constructor() {

    companion object {
        private var instance: CafeManager? = null
        fun getInstance(): CafeManager {
            if (instance == null) {
                instance = CafeManager()
            }
            return instance!!
        }
    }

    var cafes = MutableLiveData<ArrayList<Cafe>>()

    fun getFavoriteCafeList():ArrayList<Cafe> {
        return arrayListOf()
    }
}
