package edu.skku.map.capstone.view.home.cafelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.FragmentCafeListBinding
import edu.skku.map.capstone.manager.CafeDetailManager
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.view.home.HomeFragment
import edu.skku.map.capstone.view.home.HomeViewModel


class CafeListFragment() : Fragment() {
    private var _binding: FragmentCafeListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : HomeViewModel
    private lateinit var cafeListAdapter: CafeListAdapter

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
        cafeListAdapter = CafeListAdapter(requireParentFragment().requireContext())
        Log.d("cafe", "default cafe list:" + viewModel.liveCafeList.value.toString())
        binding.cafeListRV.adapter = cafeListAdapter
    }
    private fun observeCafeList() {
        viewModel.liveCafeList.observe(viewLifecycleOwner) { cafeList ->
            cafeList?.let {
                cafeListAdapter.updateCafeList(it)
            }
            if(cafeList.isEmpty()){
                binding.cafeListRV.visibility = RecyclerView.GONE
                binding.noDataView.visibility = LinearLayout.VISIBLE
            }
            else{
                binding.cafeListRV.visibility = RecyclerView.VISIBLE
                binding.noDataView.visibility = LinearLayout.GONE
            }
        }
    }
    private fun observeCafeClick() {
        CafeDetailManager.getInstance().currentViewingCafe.observe(viewLifecycleOwner) {
            val originalCafeList = viewModel.liveCafeList.value!!
            cafeListAdapter.updateCafeList(listWhenCafeClicked(originalCafeList))
        }
    }

    private fun listWhenCafeClicked(cafeList: ArrayList<Cafe>):ArrayList<Cafe> {
        val cafe = CafeDetailManager.getInstance().currentViewingCafe.value
        val modifiedList:ArrayList<Cafe> = arrayListOf()
        modifiedList.addAll(cafeList)
        if(cafe != null) {
            modifiedList.remove(cafe)
            modifiedList.add(0,cafe)
        }
        return modifiedList
    }

}