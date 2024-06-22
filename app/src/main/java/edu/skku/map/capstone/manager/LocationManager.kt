package edu.skku.map.capstone.manager
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng

val DEFAULT_LAT = 37.402005
val DEFAULT_LNG = 127.108621
class MyLocationManager private constructor() {

    companion object {
        private var instance: MyLocationManager? = null
        fun getInstance(): MyLocationManager {
            if (instance == null) {
                instance = MyLocationManager()
            }
            return instance!!
        }
    }

    var latLng = MutableLiveData(LatLng.from(DEFAULT_LAT, DEFAULT_LNG))
}
