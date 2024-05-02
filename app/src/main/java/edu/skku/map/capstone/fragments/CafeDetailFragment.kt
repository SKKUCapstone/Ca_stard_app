package edu.skku.map.capstone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.FragmentCafeDetailBinding
import edu.skku.map.capstone.databinding.FragmentCafeListBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewmodels.HomeViewModel

class CafeDetailFragment(_cafe: Cafe) : Fragment() {
    private var _binding: FragmentCafeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel = HomeViewModel()
    private val cafe = _cafe
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCafeDetailBinding.inflate(inflater, container, false)
        initUI()
        handleClickListeners()
        return binding.root
    }

    private fun initUI() {
        binding.detailCafeNameTV.text = cafe.cafeName
        binding.detailCafeName2TV.text = cafe.cafeName
        binding.detailAddressTV.text = cafe.address
        binding.detailPhoneTV.text = cafe.phone
        binding.detailDistanceTV.text = cafe.distance.toString()+"KM"

    }

    private fun handleClickListeners() {
        binding.backBtn.setOnClickListener {
            val parentFragment: HomeFragment = parentFragment as HomeFragment
            parentFragment.onCafeDetailClosed()

        }
    }
}