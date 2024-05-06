package edu.skku.map.capstone.viewmodels

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.Label
import edu.skku.map.capstone.RetrofitService
import edu.skku.map.capstone.fragments.CafeDetailFragment
import edu.skku.map.capstone.fragments.CafeListFragment
import edu.skku.map.capstone.models.Cafe
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel() {
    private val _liveCafeList: MutableLiveData<List<Cafe>> = MutableLiveData<List<Cafe>>() //viewModel 안에서만 수정
    val liveCafeList: LiveData<List<Cafe>> get() = _liveCafeList //뷰모델 밖에서 수정
    private val _liveLatLng:MutableLiveData<LatLng> = MutableLiveData<LatLng>()
    val liveLatLng:LiveData<LatLng> get() = _liveLatLng
    lateinit var cafeListFragment: CafeListFragment
    var cafeDetailFragment: CafeDetailFragment? = null
    var prevCafeDetailFragment: CafeDetailFragment? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    lateinit var activity: Activity
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    private val DEFAULT_RADIUS = 500

    init {
        _liveCafeList.value = listOf()
        _liveLatLng.value = LatLng.from(DEFAULT_LAT,DEFAULT_LNG)
    }

    fun fetchCafes(lat:Double?, lng: Double?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d("cafe","fetching cafes from (${lat?.toString()?:DEFAULT_LNG.toString()},${lng?.toString()?:DEFAULT_LAT.toString()})")

        service
            .getCafes(
                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
                "CE7",
                lat?.toString()?:DEFAULT_LNG.toString(),
                lng?.toString()?:DEFAULT_LAT.toString(),
                DEFAULT_RADIUS
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
                        val cafe = Cafe(cafeJsonObject)
                        newCafeList.add(cafe)
                    }
                    _liveCafeList.value = newCafeList
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                }
            })
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
}