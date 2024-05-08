package edu.skku.map.capstone.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewmodels.HomeViewModel
import edu.skku.map.capstone.viewmodels.MainViewModel


class HomeFragment() : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val viewModel = HomeViewModel()
    lateinit var kakaoMap: KakaoMap
    private lateinit var camera: CameraPosition
    var currentLabel: Label? = null
    private lateinit var labelManager: LabelManager
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var lodLabels: Array<LodLabel>
    val DEFAULT_LAT = 37.402005
    val DEFAULT_LNG = 127.108621

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViewModel()
        viewModel.listenLocation()
        initUI()
        listenBottomSheetEvent()
        handleBtnEvent()
        initKakaoMap()
        viewModel.fetchCurrentLocation()
        viewModel.fetchCafes(null,null)
        return binding.root
    }

    private fun initViewModel() {
        viewModel.cafeListFragment = CafeListFragment()
        viewModel.activity = requireActivity()
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
            moveCamera(viewModel.liveLatLng.value!!.latitude,viewModel.liveLatLng.value!!.longitude)
        }
        binding.relocateBtn.setOnClickListener {
            val newPos = kakaoMap.cameraPosition?.position
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
                labelManager.layer!!.zOrder = 10002
                labelManager.lodLayer!!.zOrder = 10001
                camera = kakaoMap.cameraPosition!!

                observeCafeList()
                observeLocation()
                setLabelClickListener()
            }
        })
    }

    private fun moveCamera(lat: Double, lng: Double){
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lng))
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(100, true, true))
    }

    private fun createMyLabel(lat: Double, lng: Double): Label {
        val pos:LatLng = LatLng.from(lat, lng)
        val labelStyle: LabelStyle = LabelStyle.from(R.drawable.mypin)
        val labelStyles: LabelStyles = labelManager.addLabelStyles(LabelStyles.from(labelStyle))!!
        val labelOptions:LabelOptions = LabelOptions.from(pos).setStyles(labelStyles)
        return labelManager.layer!!.addLabel(labelOptions)
    }
    private fun createCafeLabel(lat:Double, lng:Double) { //for test
        val style:LabelStyles = LabelStyles.from(LabelStyle.from(R.drawable.defaultcafepin))
        val options:LabelOptions = LabelOptions.from(LatLng.from(lat, lng)).setStyles(style)
        val layer = kakaoMap.labelManager!!.lodLayer
        val label = layer!!.addLodLabel(options)
    }
    private fun updateCafeLabels() {
        if(!::kakaoMap.isInitialized) {
            Log.d("label", "kakaomap not initialized")
            return
        } else if(viewModel.liveCafeList.value!!.isEmpty()){
            Log.d("label","cafe list is empty")
            return
        }
        val layer = kakaoMap.labelManager!!.lodLayer
        layer?.removeAll()
        val style:LabelStyles = LabelStyles.from(LabelStyle.from(R.drawable.defaultcafepin))
        val options:List<LabelOptions> = viewModel.liveCafeList.value!!.map { LabelOptions.from(LatLng.from(it.latitude!!, it.longitude!!)).setStyles(style) }
        Log.d("label","options: "+options.toString())
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
                label.changeStyles(LabelStyles.from(LabelStyle.from(R.drawable.defaultcafepin_clicked)),true)
                onCafeDetailOpen(getCafeByLabelId(label.labelId))
            }
        )
    }

    fun onCafeDetailOpen(cafe:Cafe){
        onCafeDetailClosed() //remove possibly existing detailFragment
        viewModel.cafeDetailFragment = CafeDetailFragment(cafe, (requireActivity() as MainActivity).reviewPhase)

        childFragmentManager.beginTransaction().apply {
            add(binding.childFL.id, viewModel.cafeDetailFragment as Fragment).commit()
        }
        viewModel.prevCafeDetailFragment = viewModel.cafeDetailFragment
        if(BottomSheetBehavior.from(binding.bottomSheet).state == BottomSheetBehavior.STATE_COLLAPSED) {
            BottomSheetBehavior.from(binding.bottomSheet).state =
                BottomSheetBehavior.STATE_HALF_EXPANDED
        }

    }
    fun onCafeDetailClosed() {
        //there was no detailfragment
        if(viewModel.prevCafeDetailFragment == null) {
            return
        }
        childFragmentManager.beginTransaction().apply {
            remove(viewModel.prevCafeDetailFragment as Fragment).commit()
        }
        viewModel.prevCafeDetailFragment = null

    }

    private fun getCafeByLabelId(labelId: String):Cafe {
        val targetLodLabel = lodLabels.find {
            it.labelId == labelId
        }
        return viewModel.liveCafeList.value?.get(lodLabels.indexOf(targetLodLabel))!!
    }

    private fun observeCafeList() {
        viewModel.liveCafeList.observe(viewLifecycleOwner) { cafeList ->
            cafeList?.let {
                updateCafeLabels()
            }
        }
    }

    private fun observeLocation() {
        val lat = viewModel.liveLatLng.value!!.latitude
        val lng = viewModel.liveLatLng.value!!.longitude
        if (currentLabel == null) {
            currentLabel = createMyLabel(lat,lng)
        } else {
            currentLabel!!.moveTo(LatLng.from(lat,lng))
        }
        moveCamera(lat,lng)
    }
}


















