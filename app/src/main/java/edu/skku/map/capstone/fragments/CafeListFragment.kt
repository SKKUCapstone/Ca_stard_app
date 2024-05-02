package edu.skku.map.capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import edu.skku.map.capstone.R
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.databinding.FragmentCafeListBinding
import edu.skku.map.capstone.viewmodels.HomeViewModel


class CafeListFragment() : Fragment() {
    private var _binding: FragmentCafeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel = HomeViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCafeListBinding.inflate(inflater, container, false)
//        initUI()

        return binding.root
    }

    private fun initUI(){
        binding.cafeListRV.adapter = viewModel.cafeListAdapter
        binding.cafeListRV.layoutManager = LinearLayoutManager(requireActivity())

    }


}