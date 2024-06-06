package edu.skku.map.capstone.view.recommend

import android.annotation.SuppressLint
import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater, container, false)

        setUI()
        observeRecommendList()
        return binding.root
    }

    private fun setUI() {
        binding.recommListRV.adapter = RecommendCafeListAdapter(requireContext(), recommendCafeList.value!!)
    }

    private fun fetchRecommendation() {
        //TODO: fetch cafe data
        val fetchedCafes = listOf<Cafe>()
        recommendCafeList.postValue(fetchedCafes)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecommendList() {
        recommendCafeList.observe(context as LifecycleOwner) {
            binding.recommListRV.adapter!!.notifyDataSetChanged()
        }
    }

}