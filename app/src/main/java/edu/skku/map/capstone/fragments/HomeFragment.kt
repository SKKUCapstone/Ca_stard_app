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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.TrackingManager
import com.kakao.vectormap.shape.MapPoints
import edu.skku.map.capstone.R
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.models.dummyCafeData

class HomeFragment() : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var labelManager:LabelManager
    private lateinit var trackingManager:TrackingManager
    private val cafeList: ArrayList<Cafe> = arrayListOf()
    private lateinit var cafeListAdapter: CafePreviewListAdapter
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private var myPosition:Label? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setLocationManager()
        setListener()
        setLocationListener()
        startKakaoMap()
        persistentBottomSheetEvent()
        setUI()
        return binding.root
    }

    //kakaomap functions
    private fun startKakaoMap(){
        binding.kakaoMV.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                Log.d("@@@ kakao map error", error.toString())
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("@@@ kakao map ready", "!")
                labelManager = kakaoMap.labelManager!!
                trackingManager = kakaoMap.trackingManager!!
            }
        })
    }
    private fun createLabel(lat: Double, lng: Double, text: String? = null): Label {
        val pos:LatLng = LatLng.from(lat, lng)
        val labelStyle: LabelStyle = LabelStyle.from(R.drawable.mypin)
        val labelStyles: LabelStyles = labelManager.addLabelStyles(LabelStyles.from(labelStyle))!!
        val labelOptions:LabelOptions = if (text == null) LabelOptions.from(pos).setStyles(labelStyles) else
            LabelOptions.from(pos).setStyles(labelStyles).setTexts(text)

        return labelManager.layer!!.addLabel(labelOptions)
    }

    //GPS functions
    private fun setLocationManager(){
        Log.d("gps", "initializeLocationObj")
        if(::locationManager.isInitialized.not()) {
            locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        }
    }
    private fun setLocationListener() {
        if(::locationListener.isInitialized.not()) {
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.d("gps", "onLocationChanged")
                    var lat = 0.0
                    var lng = 0.0
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                        Log.d("gps", "lat: $lat, lng: $lng")
                    }
                    if(myPosition == null){
                        myPosition = createLabel(lat, lng, "내위치")
                        val point = MapPoints.fromLatLng(LatLng.from(lat,lng))


//                        trackingManager.startTracking(myPosition)
                    }
                    else{
                        myPosition!!.moveTo(LatLng.from(lat,lng))
                    }
                }

                override fun onProviderEnabled(provider: String) {
                    Log.d("gps", "onProviderEnabled")
                }

                override fun onProviderDisabled(provider: String) {}
            }
        }


    }
    private fun getCurrentLocation() {
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

        }
        else{
            Log.d("gps","permission needed")
        }

    }
    private fun setListener() {
        binding.gpsBtn.setOnClickListener {
            getCurrentLocation()
        }
    }

    //bottomsheet functions
    private fun persistentBottomSheetEvent() {

        behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 되는 도중 계속 호출
                // called continuously while dragging
//                Log.d("@@@", "onStateChanged: 드래그 중")
            }

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

    //UI functions
    private fun setUI() {
        //TODO: replace to fetchData
        cafeList.addAll(dummyCafeData)

        cafeListAdapter = CafePreviewListAdapter(requireActivity(), cafeList)
        binding.cafeListRV.adapter = cafeListAdapter
        binding.cafeListRV.layoutManager = LinearLayoutManager(requireActivity())
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCafeListList(cafeList: ArrayList<Cafe>) {
        cafeList.clear()
        cafeList.addAll(cafeList)
        cafeListAdapter.notifyDataSetChanged()
    }

}