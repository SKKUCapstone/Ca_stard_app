package edu.skku.map.capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.FragmentHomeBinding
import edu.skku.map.capstone.databinding.FragmentMyCafeBinding


class MyCafeFragment : Fragment() {
    private var _binding: FragmentMyCafeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyCafeBinding.inflate(inflater, container, false)
        initPieChart()

        return binding.root
    }
    private fun initPieChart(){

        val reviewRatio = listOf(
            PieEntry(25f),
            PieEntry(40f),
            PieEntry(35f),
            )
        val pieColors = listOf(
            R.color.yellow,
            R.color.green,
            R.color.orange
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
            isRotationEnabled = true
            holeRadius = 60f
            setTouchEnabled(false)
            animateY(1200, Easing.EaseInOutCubic)

            // animate()를 호출하면 차트를 새로 고치기 위해 invalidate()를 호출할 필요가 없다.
            animate()
        }
    }

}