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
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.view.home.detail.CafeDetailFragment
import edu.skku.map.capstone.view.home.cafelist.CafeListFragment
import edu.skku.map.capstone.models.cafe.Cafe
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class HomeViewModel() {
    private val _liveCafeList: MutableLiveData<List<Cafe>> = MutableLiveData<List<Cafe>>() //viewModel 안에서만 수정
    val liveCafeList: LiveData<List<Cafe>> get() = _liveCafeList //뷰모델 밖에서 수정
    private val _liveLatLng:MutableLiveData<LatLng> = MutableLiveData<LatLng>()
    val liveLatLng:LiveData<LatLng> get() = _liveLatLng
    val filterCategory = MutableLiveData<ArrayList<String>>(arrayListOf())
    val searchText = MutableLiveData("")
    val zoomLevel = MutableLiveData(1)
    var radius = 20000
    lateinit var cafeListFragment: CafeListFragment
    var cafeDetailFragment: CafeDetailFragment? = null
    var prevCafeDetailFragment: CafeDetailFragment? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    lateinit var activity: Activity
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621

    init {
        _liveCafeList.value = listOf()
        _liveLatLng.value = LatLng.from(DEFAULT_LAT,DEFAULT_LNG)
    }

    fun fetchCafes(lat:Double?, lng: Double?, radius: Int) {
        val filter:String? = filterCategory.value?.joinToString(separator = ",")

        val retrofit = Retrofit.Builder()
//            .baseUrl("https://dapi.kakao.com/")
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d("cafe","fetching cafes from (${lat?.toString()?:DEFAULT_LNG.toString()},${lng?.toString()?:DEFAULT_LAT.toString()})")
//        service
//            .getCafes(
//                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
//                "CE7",
//                lat?.toString()?:DEFAULT_LNG.toString(),
//                lng?.toString()?:DEFAULT_LAT.toString(),
//                radius
//            )
        service
            .getCafes(
                (lng?:DEFAULT_LNG).toString(),
                (lat?:DEFAULT_LAT).toString(),
                radius,
                filter,
                if(searchText.value!!.trim() == "") null else searchText.value,
            )
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val newCafeList = mutableListOf<Cafe>()
                    val body = response.body()!!
                    val jsonObject = JSONObject(body.string())
                    val cafeData = jsonObject.getJSONArray("documents")

                    for (i in 0 until cafeData.length()) {
                        val cafeJsonObject = cafeData.getJSONObject(i)
                        Log.d("cafe", cafeJsonObject.toString())
                        val cafe = Cafe(cafeJsonObject)
                        newCafeList.add(cafe)
                    }
                    Log.d("cafe","total ${newCafeList.size} cafe fetched:"+newCafeList.toString())
                    _liveCafeList.value = newCafeList
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                }
            })
    }

    fun observeSearchText() {
        searchText.observe(activity as LifecycleOwner) {
            fetchCafes(null, null, radius)
        }
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
            Log.d("gps","requestLocationUpdates")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1500L,
                30f,
                locationListener
            )
        } else {
            Log.d("gps","permission needed")
        }
    }

    fun listenLocation() {
        if(::locationManager.isInitialized.not()) {
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
//                    val newLat = location.latitude
//                    val newLng = location.longitude
                    val newLat = DEFAULT_LAT
                    val newLng = DEFAULT_LNG
                    Log.d("gps", "lat: $newLat, lng: $newLng")
                    _liveLatLng.value = LatLng.from(newLat,newLng)
                }
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
        }
    }

    fun calculateDistance(point1: LatLng, point2: LatLng): Double {
        val earthRadius = 6371 // Radius of the Earth in kilometers

        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        val dlon = lon2 - lon1
        val dlat = lat2 - lat1

        val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c // Distance in kilometers
    }


 }