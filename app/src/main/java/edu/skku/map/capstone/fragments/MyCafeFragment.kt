package edu.skku.map.capstone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import edu.skku.map.capstone.R
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.adapters.MyCafeLikeListAdapter
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.databinding.FragmentMyCafeBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewmodels.HomeViewModel
import edu.skku.map.capstone.viewmodels.MyCafeViewModel


class MyCafeFragment : Fragment() {
    private var _binding: FragmentMyCafeBinding? = null
    private val binding get() = _binding!!
    val viewModel = MyCafeViewModel()

    // #2. 이런곳은 어떤가요?
    private lateinit var cafeListAdapter: CafePreviewListAdapter
    private val onCafeClick = MutableLiveData<Cafe>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyCafeBinding.inflate(inflater, container, false)
        viewModel.fetchCafes(null,null)
        initUI()
        observeDataList()


        return binding.root
    }

    private fun initUI() {
        cafeListAdapter = CafePreviewListAdapter(requireContext(), onCafeClick)
        binding.myCafeRecommendListRV.adapter = cafeListAdapter
        binding.myCafeVisitedListRV.adapter = cafeListAdapter
        initPieChart()
    }

    private fun observeDataList() {
        viewModel.recommendCafeList.observe(viewLifecycleOwner) { recommendCafeList ->
            cafeListAdapter.updateCafeList(recommendCafeList)
        }
        viewModel.visitedCafeList.observe(viewLifecycleOwner) { visitedCafeList ->
            cafeListAdapter.updateCafeList(visitedCafeList)
        }
    }
    private fun initPieChart(){
        binding.pieChart.setUsePercentValues(true)
        val reviewRatio = listOf(
            PieEntry(30f,"넓이"),
            PieEntry(40f,"밝기"),
            PieEntry(30f,"위생"),
//            PieEntry(12.5f,"와이파이"),
//            PieEntry(12.5f,"소음"),
//            PieEntry(12.5f,"책상"),
//            PieEntry(12.5f,"화장실"),
//            PieEntry(12.5f,"넓이"),
            )
        val pieColors = listOf(
            resources.getColor(R.color.yellow, null),
            resources.getColor(R.color.orange, null),
            resources.getColor(R.color.green, null)
        )
        val dataSet = PieDataSet(reviewRatio, "preferences")

        // slice의 색상을 설정해준다.
        dataSet.colors = pieColors

        // true로 설정하면 slice 위에 Entry로 설정한 값이 보여진다.
        // 만들어야하는 디자인에는 값이 필요없어 false로 설정해줬다.
        dataSet.setDrawValues(false)

        // description.isEnabled : 차트 설명 유무 설정
        // legend.isEnabled : 범례 유무 설정
        // isRotationEnabled : 차트 회전 활성화 여부 설정
        // holeRadius : 차트 중간 구멍 크기 설정
        // setTouchEnabled : slice 터치 활성화 여부 설정
        // animateY(1200, Easing.EaseInOutCubic) : 애니메이션 시간, 효과 설정
        binding.pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            isRotationEnabled = false
            holeRadius = 60f
            setTouchEnabled(false)
            animateY(1200, Easing.EaseInOutCubic)

            // animate()를 호출하면 차트를 새로 고치기 위해 invalidate()를 호출할 필요가 없다.
            animate()
        }
    }

}