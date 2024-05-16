package edu.skku.map.capstone.view.home.cafelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.databinding.FragmentCafeListBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.view.home.HomeFragment
import edu.skku.map.capstone.view.home.HomeViewModel


class CafeListFragment() : Fragment() {
    private var _binding: FragmentCafeListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : HomeViewModel
    private lateinit var cafeListAdapter: CafeListAdapter
    private val onCafeClick = MutableLiveData<Cafe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCafeListBinding.inflate(inflater, container, false)
        initViewModel()
        initUI()
        observeCafeList()
        observeCafeClick()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = (parentFragment as HomeFragment).viewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUI() {
        cafeListAdapter = CafeListAdapter(requireParentFragment().requireContext(), onCafeClick)

        Log.d("cafe", "default cafe list:" + viewModel.liveCafeList.value.toString())
        binding.cafeListRV.adapter = cafeListAdapter
    }
    private fun observeCafeList() {
        viewModel.liveCafeList.observe(viewLifecycleOwner) { cafeList ->
            cafeList?.let {
                cafeListAdapter.updateCafeList(it)
            }
        }
    }
    private fun observeCafeClick() {
        onCafeClick.observe(viewLifecycleOwner) {
            Log.d("cafe click", it.toString())
            (parentFragment as HomeFragment).onCafeDetailOpen(it)
            ((parentFragment as HomeFragment).activity as MainActivity).reviewingCafe.postValue(it)
        }
    }

}