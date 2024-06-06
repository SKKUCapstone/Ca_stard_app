package edu.skku.map.capstone.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.databinding.FragmentFavoriteBinding
import edu.skku.map.capstone.models.cafe.Cafe

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val _recommendCafeList: MutableLiveData<List<Cafe>> = MutableLiveData<List<Cafe>>() // 추천 카페

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setUI()
        return binding.root
    }

    private fun setUI() {

    }

}