package edu.skku.map.capstone.fragments

import android.Manifest
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.label.LodLabelLayer
import edu.skku.map.capstone.R
import edu.skku.map.capstone.RetrofitService
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewmodels.HomeViewModel
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
    val viewModel : HomeViewModel = HomeViewModel()
    lateinit var kakaoMap: KakaoMap
    private lateinit var camera: CameraPosition
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var labelManager: LabelManager
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var lodLabels: Array<LodLabel>

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
        initViewModel()
        initUI()
        listenBottomSheetEvent()
        handleBtnEvent()
        initKakaoMap()
        listenLocation()
        fetchCurrentLocation()
        viewModel.fetchCafes(null,null)
        return binding.root
    }

    private fun initViewModel() {
        viewModel.cafeListFragment = CafeListFragment()
    }

    private fun initUI() {
        childFragmentManager.beginTransaction().apply {
            replace(binding.childFL.id,viewModel.cafeListFragment).commit()
        }
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
                viewModel.fetchCafes(newPos.latitude, newPos.longitude)
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

                updateCafeLabels()
                setLabelClickListener()

            }
        })
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
        kakaoMap?.moveCamera(cameraUpdate, CameraAnimation.from(100, true, true))
    }

    private fun createMyLabel(lat: Double, lng: Double): Label {
        val pos:LatLng = LatLng.from(lat, lng)
        val labelStyle: LabelStyle = LabelStyle.from(edu.skku.map.capstone.R.drawable.mypin)
        val labelStyles: LabelStyles = labelManager.addLabelStyles(LabelStyles.from(labelStyle))!!
        val labelOptions:LabelOptions = LabelOptions.from(pos).setStyles(labelStyles)
        return labelManager.layer!!.addLabel(labelOptions)
    }
    private fun createCafeLabel(lat:Double, lng:Double) { //for test
        val style:LabelStyles = LabelStyles.from(LabelStyle.from(edu.skku.map.capstone.R.drawable.defaultcafepin))
        val options:LabelOptions = LabelOptions.from(LatLng.from(lat, lng)).setStyles(style)
        val layer = kakaoMap.labelManager!!.lodLayer
        val label = layer!!.addLodLabel(options)
    }
    private fun updateCafeLabels() {
        if(!::kakaoMap.isInitialized) {
            Log.d("label", "kakaomap not initialized")
            return
        }
        val layer = kakaoMap.labelManager!!.lodLayer
        layer?.removeAll()
        val style:LabelStyles = LabelStyles.from(LabelStyle.from(edu.skku.map.capstone.R.drawable.defaultcafepin))
        val options:List<LabelOptions> = viewModel.liveCafeList.value!!.map { LabelOptions.from(LatLng.from(it.latitude!!, it.longitude!!)).setStyles(style) }
        lodLabels = layer!!.addLodLabels(options)
    }
    private fun setLabelClickListener(){
        kakaoMap.setOnLabelClickListener(
            fun (kakaoMap: KakaoMap, layer:LabelLayer, label:Label){
                Log.d("cafe", "$label label clicked")
            }
        )
        kakaoMap.setOnLodLabelClickListener(
            fun (kakaoMap: KakaoMap, layer: LodLabelLayer, label: LodLabel){
                Log.d("cafe", "$label label clicked")
                viewModel.cafeDetailFragment = CafeDetailFragment(getCafeByLabelId(label.labelId))
                onCafeDetailOpen()
            }
        )
    }

    private fun onCafeDetailOpen(){
        Log.d("cafe",childFragmentManager.findFragmentById(R.id.childFL).toString())
        if(childFragmentManager.findFragmentById(R.id.childFL) !is CafeDetailFragment) {
            childFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.animator.to_right, R.animator.from_left)
                add(binding.childFL.id, viewModel.cafeDetailFragment).commit()
            }
        }
        BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HALF_EXPANDED

    }
    fun onCafeDetailClosed() {
        Log.d("test",childFragmentManager.toString())
        childFragmentManager.beginTransaction().apply {
            remove(viewModel.cafeDetailFragment).commit()
        }
    }
    private fun getCafeByLabelId(labelId: String):Cafe {
        val targetLodLabel = lodLabels.find {
            it.labelId == labelId
        }
        return viewModel.liveCafeList.value?.get(lodLabels.indexOf(targetLodLabel))!!
    }

}


















