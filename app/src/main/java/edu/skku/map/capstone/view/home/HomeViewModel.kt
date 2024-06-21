package edu.skku.map.capstone.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.manager.MyLocationManager
import edu.skku.map.capstone.view.home.cafelist.CafeListFragment
import edu.skku.map.capstone.models.cafe.Cafe

class HomeViewModel() {
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    private val _liveCafeList: MutableLiveData<ArrayList<Cafe>> = MutableLiveData<ArrayList<Cafe>>()
    var lastSearchedLat: Double
    var lastSearchedLng: Double

    val liveCafeList: LiveData<ArrayList<Cafe>> get() = _liveCafeList //뷰모델 밖에서 수정
    val filterCategory = MutableLiveData<ArrayList<String>>(arrayListOf())
    var isFilterCategoryInitialized = false
    val searchText: MutableLiveData<String> = MutableLiveData("")
    var isSearchTextInitialized = false
    var radius = 400
    lateinit var cafeListFragment: CafeListFragment
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var activity: Activity


    init {
        _liveCafeList.value = arrayListOf()
        lastSearchedLat = DEFAULT_LAT
        lastSearchedLng = DEFAULT_LNG
    }


    fun fetchCafes(lat: Double?, lng: Double?, radius: Int) {
//        val db = Firebase.firestore
//        db.collection("cafe")
//            .get()
//            .addOnSuccessListener { documents ->
//                val allCafes = arrayListOf<Cafe>()
//                for (document in documents) {
//                    allCafes.add(Cafe(document))
//                }
//                val retrofit = Retrofit.Builder()
//                    .baseUrl("https://dapi.kakao.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                val service = retrofit.create(RetrofitService::class.java)
//
//                Log.d(
//                    "cafe",
//                    "fetching cafes from (${lat?.toString() ?: DEFAULT_LNG.toString()},${lng?.toString() ?: DEFAULT_LAT.toString()})"
//                )
//
//                service
//                    .getCafes(
//                        "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
//                        "CE7",
//                        lat?.toString()?:DEFAULT_LNG.toString(),
//                        lng?.toString()?:DEFAULT_LAT.toString(),
//                        radius
//                    )
//                    .enqueue(object : Callback<ResponseBody> {
//                        @SuppressLint("NotifyDataSetChanged")
//                        override fun onResponse(
//                            call: Call<ResponseBody>,
//                            response: Response<ResponseBody>
//                        ) {
//                            val newCafeList = mutableListOf<Cafe>()
//                            val body = response.body()!!
//
//                            val jsonArray = JSONArray(body.string())
//                            Log.d("cafe", "cafeData ${jsonArray}")
//                            for (i in 0 until jsonArray.length()) {
//                                val cafeJsonObject = jsonArray.getJSONObject(i)
//                                Log.d("cafe", cafeJsonObject.toString())
//                                val cafe = Cafe(cafeJsonObject)
//
//                                val matchedCafeData = allCafes.find {
//                                    it.cafeId == cafe.cafeId
//                                }
//                                if(matchedCafeData == null){
//                                    newCafeList.add(cafe)
//                                }
//                                else{
//                                    newCafeList.add(matchedCafeData)
//                                }
//
//                            }
//                            Log.d(
//                                "cafe",
//                                "total ${newCafeList.size} cafe fetched:" + newCafeList.toString()
//                            )
//                            _liveCafeList.value = newCafeList
//                        }
//
//                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                            Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
//                        }
//                    })
//            }
//            .addOnFailureListener { exception ->
//                Log.w("firebase", "Error getting documents: ", exception)
//            }
    }

    fun fetchCurrentLocation() {
        Log.d("gps", "getCurrentLocation")
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("gps", "requestLocationUpdates")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1500L,
                30f,
                locationListener
            )
        } else {
            Log.d("gps", "permission needed")
        }
    }

    fun listenLocationChange() {
        if (::locationManager.isInitialized.not()) {
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
//                    val newLat = location.latitude
//                    val newLng = location.longitude
                    val newLat = DEFAULT_LAT
                    val newLng = DEFAULT_LNG
                    Log.d("@@@gps", "lat: $newLat, lng: $newLng")
                    MyLocationManager.getInstance().latLng.postValue(LatLng.from(newLat,newLng))

                }
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
        }
    }

    fun setLocation(lat: Double, lng: Double) {
        lastSearchedLat = lat
        lastSearchedLng = lng
        Log.d("homeviewmodel", "setLocation: lat:${lat} lng:${lng} ")
    }

}
