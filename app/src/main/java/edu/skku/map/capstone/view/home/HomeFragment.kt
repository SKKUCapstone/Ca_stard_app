package edu.skku.map.capstone.view.home
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.GestureType
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
import com.kakao.vectormap.label.LabelTextStyle
import com.kakao.vectormap.label.LabelTransition
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.label.LodLabelLayer
import com.kakao.vectormap.label.Transition
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.manager.CafeDetailManager
import edu.skku.map.capstone.view.home.cafelist.CafeListFragment
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.user.DEFAULT_LAT
import edu.skku.map.capstone.models.user.DEFAULT_LNG
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.calculateDistance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val viewModel = HomeViewModel()
    lateinit var kakaoMap: KakaoMap
    private var currentLabel:Label? = null
    private lateinit var camera: CameraPosition
    private lateinit var labelManager: LabelManager
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var lodLabels: Array<LodLabel>
    val pullDownBottemSheet = MutableLiveData(false)
    private var cameraLat: Double = User.getInstance().latLng.value?.latitude ?: DEFAULT_LAT
    private var cameraLng: Double = User.getInstance().latLng.value?.longitude?: DEFAULT_LNG
    private val categoryList = arrayListOf("capacity","bright","clean","wifi","quiet","tables","powerSocket","toilet")
    private var myCoroutineJob: Job = Job()
    private val myCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + myCoroutineJob

    private val lodLabelStyleIDDefault = LabelStyles.from(
        LabelStyle.from(R.drawable.defaultcafepin)
            .setZoomLevel(0)
            .setIconTransition(LabelTransition.from(Transition.None,Transition.None))
    )
    private val lodLabelStyleIDClicked = LabelStyles.from(
        LabelStyle.from(R.drawable.defaultcafepin_clicked)
            .setZoomLevel(0)
            .setTextStyles(LabelTextStyle.from(28,Color.BLACK,3, Color.WHITE))
            .setIconTransition(LabelTransition.from(Transition.None,Transition.None))
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViewModel()
        viewModel.listenLocationChange()
        initUI()
        listenBottomSheetEvent()
        setClickListener()
        initKakaoMap()
        viewModel.fetchCurrentLocation()
        observeViewingCafe()
        observeBottomSheet()
        listenEditText()
        observeFilter()
        observeSearchText()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        restoreHomeView()
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
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        pullDownBottemSheet.postValue(false)
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        pullDownBottemSheet.postValue(false)
                    }
                }
            }
        })
    }
    private fun toggleCategory(category: String) {
        val prevList = viewModel.filterCategory.value!!
        if(prevList.contains(category)) prevList.remove(category)
        else prevList.add(category)
        viewModel.filterCategory.postValue(prevList)
    }

    private fun setClickListener() {
        binding.gpsBtn.setOnClickListener {
            moveCamera(User.getInstance().latLng.value!!.latitude,User.getInstance().latLng.value!!.longitude)
            binding.relocateBtn.visibility = View.INVISIBLE
            Log.d("@@@cafefetch", "cameramove out cafe fetch")
            viewModel.fetchCafes(User.getInstance().latLng.value!!.latitude, User.getInstance().latLng.value!!.longitude, viewModel.radius)
            updateCafeLabels()
            if(behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        binding.relocateBtn.setOnClickListener {
            val newPos = kakaoMap.cameraPosition?.position
            if(newPos != null) {
                viewModel.fetchCafes(newPos.latitude, newPos.longitude, viewModel.radius)
                updateCafeLabels()
                binding.relocateBtn.visibility = View.INVISIBLE
            }
        }
        val btnList = arrayListOf(
            binding.capacityBtn,
            binding.brightBtn,
            binding.cleanBtn,
            binding.wifiBtn,
            binding.quietBtn,
            binding.tablesBtn,
            binding.powerSocketBtn,
            binding.toiletBtn,
        )
        btnList.indices.map { idx->
            btnList[idx].setOnClickListener {
                toggleCategory(categoryList[idx])
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

                observeLocation()
                observeCafeList()
                setLabelClickListener()
                listenCamera()
            }
        })
    }

    private fun moveCamera(lat: Double?, lng: Double?){
        if(::kakaoMap.isInitialized.not()){
            Log.d("@@@camera", "kakaomap is not initialized")
            return
        } else if(lat==null || lng==null) {
            Log.d("@@@camera", "latlng is null")
            return
        }
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lng))
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(100, true, true))
    }

    private fun listenCamera() {
        kakaoMap.setOnCameraMoveEndListener { kakaoMap, position, gestureType ->
            if(gestureType == GestureType.Pan) {
                val dist =
                    calculateDistance(position.position, User.getInstance().latLng.value!!)
                if (dist >= 0.1) {
                    Log.d("camera", position.position.toString())
                    binding.relocateBtn.visibility = View.VISIBLE
                }
                else{
                    binding.relocateBtn.visibility = View.INVISIBLE
                }
                cameraLat = position.position.latitude
                cameraLng = position.position.longitude
            }
            else if(gestureType == GestureType.Zoom) {
                Log.d("camera", "zoomLevel: "+kakaoMap.zoomLevel)
                if(kakaoMap.zoomLevel >= 17) viewModel.radius = 50
                else if(kakaoMap.zoomLevel == 16) viewModel.radius = 200
                else if(kakaoMap.zoomLevel == 15) viewModel.radius = 500
                else if(kakaoMap.zoomLevel == 14) viewModel.radius = 800
                else if(kakaoMap.zoomLevel == 13) viewModel.radius = 1100
                else if(kakaoMap.zoomLevel == 12) viewModel.radius = 1400
            }
        }
    }

    private fun createMyLabel(lat: Double, lng: Double): Label {
        val pos:LatLng = LatLng.from(lat, lng)
        val labelStyle: LabelStyle = LabelStyle.from(R.drawable.mypin)
        val labelStyles: LabelStyles = labelManager.addLabelStyles(LabelStyles.from(labelStyle))!!
        val labelOptions: LabelOptions = LabelOptions.from(pos).setStyles(labelStyles)
        return labelManager.layer!!.addLabel(labelOptions)
    }

    private fun updateCafeLabels() {
        if(!::kakaoMap.isInitialized) {
            Log.d("label", "kakaomap not initialized")
            return
        }
        val layer = kakaoMap.labelManager!!.lodLayer
        layer?.removeAll()

        val clickedCafe = CafeDetailManager.getInstance().currentViewingCafe.value
        Log.d("@@@cafefetch", "currentViewingCafe: ${clickedCafe.toString()}")
        val options = viewModel.liveCafeList.value!!
            .map { LabelOptions
                .from(LatLng.from(it.latitude, it.longitude))
                .setStyles(if(clickedCafe != it) lodLabelStyleIDDefault else lodLabelStyleIDClicked)
                .setTexts(it.cafeName)
                .setTag(it.cafeId)
            }
        if(viewModel.liveCafeList.value!!.isNotEmpty()) lodLabels = layer!!.addLodLabels(options)
    }
    private fun setLabelClickListener(){
        kakaoMap.setOnLabelClickListener(
            fun (kakaoMap: KakaoMap, layer:LabelLayer, label:Label){
                Log.d("cafe", "$label label clicked")
            }
        )
        kakaoMap.setOnLodLabelClickListener(
            fun (kakaoMap: KakaoMap, layer: LodLabelLayer, label: LodLabel){
                val clickedCafe = getCafeByLabelId(label.labelId)

                CafeDetailManager.getInstance().viewCafe(clickedCafe)
                if(behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }
            }
        )
    }

    private fun getCafeByLabelId(labelId: String): Cafe {
        val targetLodLabel = lodLabels.find {
            it.labelId == labelId
        }
        return viewModel.liveCafeList.value?.get(lodLabels.indexOf(targetLodLabel))!!
    }

    private fun observeCafeList() {
        viewModel.liveCafeList.observe(viewLifecycleOwner) {
            if(it != null) updateCafeLabels()
        }
    }

    private fun observeLocation() {
        User.getInstance().latLng.observe(context as LifecycleOwner) {
            val lat = User.getInstance().latLng.value!!.latitude
            val lng = User.getInstance().latLng.value!!.longitude
            if (currentLabel == null) {
                currentLabel = createMyLabel(lat, lng)
            } else {
                currentLabel!!.moveTo(LatLng.from(lat, lng))
            }
            viewModel.fetchCafes(lat,lng, viewModel.radius)
            updateCafeLabels()
            moveCamera(lat, lng)
        }

    }

    private fun observeViewingCafe() {
        CafeDetailManager.getInstance().currentViewingCafe.observe(context as LifecycleOwner) {
            updateCafeLabels()
            if (it !== null) moveCamera(it.latitude!!,it.longitude!!)
        }
    }

    private fun observeBottomSheet() {
        pullDownBottemSheet.observe(context as LifecycleOwner) {
            if(it && behavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun EditText.textChangesToFlow(): Flow<CharSequence?> {
        return callbackFlow {
            val listener = object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    trySend(s)
                }
                override fun afterTextChanged(s: Editable?) { }
            }
            addTextChangedListener(listener)
            awaitClose {
                Log.d("textChangesToFlow", "removeTextChangedListener")
                removeTextChangedListener(listener)
            }
        }.onStart {
            Log.d("textChangesToFlow", "textChangesToFlow() / onStart 발동")
            emit(text)
        }
    }

    @OptIn(DelicateCoroutinesApi::class, FlowPreview::class)
    private fun listenEditText() {
        GlobalScope.launch(context = myCoroutineContext) {
            val editTextFlow = binding.searchET.textChangesToFlow()
            editTextFlow
                .debounce(700)
                .onEach {
                    viewModel.searchText.postValue(it.toString().trim())
                }
                .launchIn(this)
        }
    }

    private fun observeSearchText() {
        viewModel.searchText.observe(activity as LifecycleOwner) {
            if(!viewModel.isSearchTextInitialized){
                viewModel.isSearchTextInitialized = true
            } else {
                Log.d("@@@cafefetch", "searchtext fetch")
                viewModel.fetchCafes(null, null, viewModel.radius)
                updateCafeLabels()
            }
        }
    }

    override fun onDestroy() {
        myCoroutineContext.cancel()
        super.onDestroy()
    }

    private fun observeFilter() {
        val layoutList = listOf(
            binding.capacityBtn,
            binding.brightBtn,
            binding.cleanBtn,
            binding.wifiBtn,
            binding.quietBtn,
            binding.tablesBtn,
            binding.powerSocketBtn,
            binding.toiletBtn
        )
        val iconList = listOf(
            binding.capacityIV,
            binding.brightIV,
            binding.cleanIV,
            binding.wifiIV,
            binding.quietIV,
            binding.tablesIV,
            binding.powerSocketIV,
            binding.toiletIV
        )
        viewModel.filterCategory.observe(context as LifecycleOwner) { list->
            categoryList.indices.forEach { idx ->
                if (list.contains(categoryList[idx])) {
                    layoutList[idx].background = ContextCompat.getDrawable(requireContext(), R.drawable.categorychip)
                    iconList[idx].alpha = 1.0F
                } else{
                    layoutList[idx].background = ContextCompat.getDrawable(requireContext(), R.drawable.categorychip_faded)
                    iconList[idx].alpha = 0.3F
                }
            }
            if(!viewModel.isFilterCategoryInitialized){
                viewModel.isFilterCategoryInitialized = true
            } else {
                Log.d("@@@cafefetch", "filter change fetch")
                viewModel.fetchCafes(cameraLat, cameraLng, viewModel.radius)
                updateCafeLabels()
            }
        }
    }

    private fun restoreHomeView() {
        //restore camera
        moveCamera(
            CafeDetailManager.getInstance().currentViewingCafe.value?.latitude,
            CafeDetailManager.getInstance().currentViewingCafe.value?.longitude
        )
        //fetch cafes by same request
        Log.d("@@@cafefetch", "onResume fetch")
        viewModel.fetchCafes(viewModel.lastSearchedLat,viewModel.lastSearchedLng,viewModel.radius)
//        updateCafeLabels()

    }
}


















