package edu.skku.map.capstone.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import edu.skku.map.capstone.RetrofitService
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.models.Cafe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment() : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
//    private var kakaoMap: KakaoMap? = null
    lateinit var kakaoMap: KakaoMap
    private lateinit var camera: CameraPosition
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var labelManager: LabelManager
    private val cafeList: ArrayList<Cafe> = arrayListOf()
    private lateinit var cafeListAdapter: CafePreviewListAdapter
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var currentLabel:Label? = null


    val DEFAULT_LAT = 37.402005
    val DEFAULT_LNG = 127.108621
    val SAMPLE_CAFE_LAT1 = 37.403016
    val SAMPLE_CAFE_LNG1 = 127.107616
    val SAMPLE_CAFE_LAT2 = 37.400124
    val SAMPLE_CAFE_LNG2 = 127.101424
    val SAMPLE_CAFE_LAT3 = 37.404124
    val SAMPLE_CAFE_LNG3 = 127.103424
    val DEFAULT_RADIUS = 500

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initUI()
        listenBottomSheetEvent()
        handleBtnEvent()
        initKakaoMap()
        listenLocation()
        fetchCurrentLocation()
//        fetchCafes(null,null) //temporarily moved to initKakaoMap callback
//        updateCafeLabels()
        return binding.root
    }

    private fun initUI() {
        cafeListAdapter = CafePreviewListAdapter(requireActivity(),cafeList)
        binding.cafeListRV.adapter = cafeListAdapter
        binding.cafeListRV.layoutManager = LinearLayoutManager(requireActivity())
    }
    private fun listenBottomSheetEvent() {
        behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                }
            }
        })
    }
    private fun handleBtnEvent() {
        binding.gpsBtn.setOnClickListener {
            if(currentLat!=null && currentLng!=null) moveCamera(currentLat!!,currentLng!!)
        }
        binding.relocateBtn.setOnClickListener {
            val newPos = kakaoMap?.cameraPosition?.position
            if(newPos != null) {
                fetchCafes(newPos.latitude, newPos.longitude)
                updateCafeLabels()
            }

        }
    }
    private fun initKakaoMap() {
        binding.kakaoMV.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception) {}
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("@@@ kakao map ready", "!")
                this@HomeFragment.kakaoMap = kakaoMap
                labelManager = kakaoMap.labelManager!!
                camera = kakaoMap.cameraPosition!!
            }
        })
    }
    private fun createMyLabel(lat: Double, lng: Double): Label {
        val pos:LatLng = LatLng.from(lat, lng)
        val labelStyle: LabelStyle = LabelStyle.from(edu.skku.map.capstone.R.drawable.mypin)
        val labelStyles: LabelStyles = labelManager.addLabelStyles(LabelStyles.from(labelStyle))!!
        val labelOptions:LabelOptions = LabelOptions.from(pos).setStyles(labelStyles)
        return labelManager.layer!!.addLabel(labelOptions)
    }
    private fun listenLocation() {
        if(::locationManager.isInitialized.not()) {
            locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
//                    currentLat = location.latitude
//                    currentLng = location.longitude
                    currentLat = DEFAULT_LAT
                    currentLng = DEFAULT_LNG

                    Log.d("gps", "lat: $currentLat, lng: $currentLng")

                    if (currentLabel == null) {
                        currentLabel = createMyLabel(currentLat!!,currentLng!!)
                    } else {
                        currentLabel!!.moveTo(LatLng.from(currentLat!!,currentLng!!))
                    }
                    moveCamera(currentLat!! ,currentLng!!)
                }
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
        }
    }
    private fun fetchCurrentLocation() {
        Log.d("gps", "getCurrentLocation")
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
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
    private fun moveCamera(lat: Double, lng: Double){
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lng))
        kakaoMap?.moveCamera(cameraUpdate, CameraAnimation.from(100, true, true))
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCafeList(newCafeList: ArrayList<Cafe>) {
        cafeList.clear()
        cafeList.addAll(newCafeList)
        cafeListAdapter.notifyDataSetChanged()
    }
    private fun fetchCafes(lat:Double?, lng: Double?) {
        val retrofit = Retrofit.Builder()
                        .baseUrl("https://dapi.kakao.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d("apitest","start fetching cafes...")
        service
            .getCafes(
                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
                "CE7",
                lat?.toString()?:DEFAULT_LNG.toString(),
                lng?.toString()?:DEFAULT_LAT.toString(),
                DEFAULT_RADIUS
            )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val cafeList = arrayListOf<Cafe>()
                    val body = response.body()!!
                    val jsonObject = JSONObject(body.string())
                    val cafeData = jsonObject.getJSONArray("documents")

                    for (i in 0 until cafeData.length()) {
                        val cafeJsonObject = cafeData.getJSONObject(i)
                        val cafe = Cafe(cafeJsonObject)
                        cafeList.add(cafe)
                    }
                    Log.d("apitest", cafeList.toString())
                    refreshCafeList(cafeList)

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("apitest", "failed to fetch cafes: ${t.localizedMessage}")

                }
            })
    }
    private fun updateCafeLabels() {
        val layer = kakaoMap?.labelManager!!.lodLayer
        layer?.removeAll()
        val style = LabelStyles.from(LabelStyle.from(edu.skku.map.capstone.R.drawable.defaultcafepin))
        val options = cafeList.map {it -> LabelOptions.from(LatLng.from(it.latitude!!, it.longitude!!)).setStyles(style) } as List<LabelOptions>
        layer!!.addLodLabels(options)
    }

}


















