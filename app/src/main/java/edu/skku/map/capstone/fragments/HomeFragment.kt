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
import edu.skku.map.capstone.models.dummyCafeData
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
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var labelManager: LabelManager
    private lateinit var camera: CameraPosition
    private val cafeList: ArrayList<Cafe> = arrayListOf()
    private lateinit var cafeListAdapter: CafePreviewListAdapter
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var currentLabel:Label? = null
    private lateinit var kakaoMap: KakaoMap

    val DEFAULT_LAT = 37.402005
    val DEFAULT_LNG = 127.108621
    val SAMPLE_CAFE_LAT1 = 37.403016
    val SAMPLE_CAFE_LNG1 = 127.107616
    val SAMPLE_CAFE_LAT2 = 37.400124
    val SAMPLE_CAFE_LNG2 = 127.101424
    val SAMPLE_CAFE_LAT3 = 37.404124
    val SAMPLE_CAFE_LNG3 = 127.103424
    val DEFAULT_RADIUS = 300

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
        fetchCafes()

        return binding.root
    }

    private fun initUI() {
        //TODO: replace to fetchData
        cafeList.addAll(dummyCafeData)

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
                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        Log.d("@@@", "onStateChanged: 접음")
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        Log.d("@@@", "onStateChanged: 드래그")
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
//                        Log.d("@@@", "onStateChanged: 펼침")
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        Log.d("@@@", "onStateChanged: 숨기기")
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
//                        Log.d("@@@", "onStateChanged: 고정됨")
                    }
                }
            }
        })
    }

    private fun handleBtnEvent() {
        binding.gpsBtn.setOnClickListener {
            if(currentLat!=null && currentLng!=null) moveCamera(currentLat!!,currentLng!!)
        }
    }

    //kakaomap functions
    private fun initKakaoMap(){
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
//        Log.d("kakaolabel","create label")
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
//                    createCafeLabel(SAMPLE_CAFE_LAT1,SAMPLE_CAFE_LNG1)
//                    createCafeLabel(SAMPLE_CAFE_LAT2,SAMPLE_CAFE_LNG2)
//                    createCafeLabel(SAMPLE_CAFE_LAT3,SAMPLE_CAFE_LNG3)
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
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(100, true, true))
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCafeListList(newCafeList: ArrayList<Cafe>) {
        cafeList.clear()
        cafeList.addAll(newCafeList)
        cafeListAdapter.notifyDataSetChanged()
    }

    private fun createCafeLabel(lat:Double, lng:Double) {
        val styles: LabelStyles = LabelStyles.from(LabelStyle.from(edu.skku.map.capstone.R.drawable.defaultcafepin))
        val options = LabelOptions.from(LatLng.from(lat, lng)).setStyles(styles)
        val layer = kakaoMap.labelManager!!.lodLayer
        val label = layer!!.addLodLabel(options)
    }

    private fun fetchCafes() {
        val retrofit = Retrofit.Builder()
                        .baseUrl("https://dapi.kakao.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .getCafes(
                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
                "CE7",
                DEFAULT_LNG.toString(),
                DEFAULT_LAT.toString(),
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
                    refreshCafeListList(cafeList)

                    //var obj = JsonParser.parseString(resStr)
//                    Log.d("apitest", documents.toString())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("apitest", "onResponse 실패")
                }
            })
    }
}


















