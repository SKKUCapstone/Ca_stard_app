package edu.skku.map.capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        return binding.root
    }


}