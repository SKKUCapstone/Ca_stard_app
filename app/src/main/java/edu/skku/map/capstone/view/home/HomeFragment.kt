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
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.view.home.detail.CafeDetailFragment
import edu.skku.map.capstone.view.home.cafelist.CafeListFragment
import edu.skku.map.capstone.models.cafe.Cafe
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
import kotlinx.coroutines.flow.filter
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
    val DEFAULT_LAT = 37.402005
    val DEFAULT_LNG = 127.108621
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
        viewModel.listenLocation()
        initUI()
        listenBottomSheetEvent()
        setClickListener()
        initKakaoMap()
        viewModel.fetchCurrentLocation()
        viewModel.fetchCafes(null,null, viewModel.radius)
        observeReviewingCafe()
        observeBottomSheet()
        listenEditText()
        observeFilter()
        viewModel.observeSearchText()
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
            moveCamera(viewModel.liveLatLng.value!!.latitude,viewModel.liveLatLng.value!!.longitude)
            binding.relocateBtn.visibility = View.INVISIBLE
        }
        binding.relocateBtn.setOnClickListener {
            val newPos = kakaoMap.cameraPosition?.position
            if(newPos != null) {
                viewModel.fetchCafes(newPos.latitude, newPos.longitude, viewModel.radius)
                updateCafeLabels()
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

                observeCafeList()
                observeLocation()
                setLabelClickListener()
                listenCamera()
            }
        })
    }

    private fun moveCamera(lat: Double, lng: Double){
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lng))
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(100, true, true))
    }

    private fun listenCamera() {
        kakaoMap.setOnCameraMoveEndListener { kakaoMap, position, gestureType ->
            if(gestureType == GestureType.Pan) {
                val dist =
                    calculateDistance(position.position, viewModel.liveLatLng.value!!)
                if (dist >= 1.0) {
                    Log.d("camera", position.position.toString())
                    binding.relocateBtn.visibility = View.VISIBLE
                }
            }
            else if(gestureType == GestureType.Zoom) {
                Log.d("camera", "zoomLevel: "+kakaoMap.zoomLevel)
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
        } else if(viewModel.liveCafeList.value!!.isEmpty()){
            Log.d("label","cafe list is empty")
            return
        }
        val layer = kakaoMap.labelManager!!.lodLayer
        layer?.removeAll()

        val clickedCafe = (requireActivity() as MainActivity).reviewingCafe.value

        val options = viewModel.liveCafeList.value!!
            .map { LabelOptions
                .from(LatLng.from(it.latitude!!, it.longitude!!))
                .setStyles(if(clickedCafe != it) lodLabelStyleIDDefault else lodLabelStyleIDClicked)
                .setTexts(it.cafeName)
                .setTag(it.cafeId)
            }
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
                val clickedCafe = getCafeByLabelId(label.labelId)
                val reviewingCafe = (requireActivity() as MainActivity).reviewingCafe
                if(clickedCafe == reviewingCafe.value) return
                reviewingCafe.postValue(clickedCafe)
                onCafeDetailOpen(clickedCafe)
            }
        )
    }

    fun onCafeDetailOpen(cafe: Cafe){
        onCafeDetailClosed() //remove possibly existing detailFragment
        val activity = (requireActivity() as MainActivity)
        viewModel.cafeDetailFragment = CafeDetailFragment(cafe,activity.reviewingCafe, activity.reviewPhase, pullDownBottemSheet)

        childFragmentManager.beginTransaction().apply {
            add(binding.childFL.id, viewModel.cafeDetailFragment as Fragment).commit()
        }
        viewModel.prevCafeDetailFragment = viewModel.cafeDetailFragment
        if(behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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
        val lat = viewModel.liveLatLng.value!!.latitude
        val lng = viewModel.liveLatLng.value!!.longitude
        if (currentLabel == null) {
            currentLabel = createMyLabel(lat,lng)
        } else {
            currentLabel!!.moveTo(LatLng.from(lat,lng))
        }
        moveCamera(lat,lng)
    }

    private fun observeReviewingCafe() {
        (requireActivity() as MainActivity).reviewingCafe.observe(context as LifecycleOwner) {
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

    fun EditText.textChangesToFlow(): Flow<CharSequence?> {
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
                .filter { it?.trim()?.isNotEmpty()!! }
                .onEach {
                    viewModel.searchText.postValue(it.toString().trim())
                }
                .launchIn(this)
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
                }
                else{
                    layoutList[idx].background = ContextCompat.getDrawable(requireContext(), R.drawable.categorychip_faded)
                    iconList[idx].alpha = 0.3F
                }
            }
        }
    }

}


















