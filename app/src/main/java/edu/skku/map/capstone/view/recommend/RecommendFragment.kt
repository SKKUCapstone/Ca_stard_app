package edu.skku.map.capstone.view.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import edu.skku.map.capstone.databinding.FragmentRecommendBinding
import edu.skku.map.capstone.models.cafe.Cafe

class RecommendFragment : Fragment() {
    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!
    private val recommendCafeList = MutableLiveData<List<Cafe>>(listOf()) // 추천 카페
    // RecyclerView
    private lateinit var recommendCafeListAdapter: RecommendCafeListAdapter
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater, container, false)
        initUI()
//        fetchRecommendation(DEFAULT_LAT, DEFAULT_LNG, MyLocationManager.getInstance().id)
        observeRecommendList()
        return binding.root
    }

    private fun initUI() {
        recommendCafeListAdapter = RecommendCafeListAdapter(requireContext(), recommendCafeList.value!!)
        Log.d("InitUI", "recommendCafeList.value: ${recommendCafeList.value!!}")
        binding.recommendListRV.adapter = recommendCafeListAdapter
    }

    private fun fetchRecommendation(lat: Double?, lng: Double?) {
        //TODO
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecommendList() {
        recommendCafeList.observe(viewLifecycleOwner) { newCafeList ->
            Log.d("observeRecommendList", "newCafeList: $newCafeList")
            recommendCafeListAdapter.updateCafes(newCafeList)
        }
    }

}