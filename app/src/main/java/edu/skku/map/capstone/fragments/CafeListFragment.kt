package edu.skku.map.capstone.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.databinding.FragmentCafeListBinding
import edu.skku.map.capstone.viewmodels.HomeViewModel


class CafeListFragment() : Fragment() {
    private var _binding: FragmentCafeListBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel : HomeViewModel
    lateinit var cafeListAdapter: CafePreviewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCafeListBinding.inflate(inflater, container, false)
        initViewModel()
        initUI()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = (parentFragment as HomeFragment).viewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUI() {
        cafeListAdapter = CafePreviewListAdapter(
            requireParentFragment().requireContext(),
            viewModel.liveCafeList.value!!
        )

        Log.d("cafe", "default cafe list:" + viewModel.liveCafeList.value.toString())
        binding.cafeListRV.adapter = cafeListAdapter
        viewModel.liveCafeList.observe(viewLifecycleOwner) { cafeList ->
            cafeList?.let {
                cafeListAdapter = CafePreviewListAdapter(requireActivity(), it)
                binding.cafeListRV.adapter = cafeListAdapter
            }
        }
    }
}