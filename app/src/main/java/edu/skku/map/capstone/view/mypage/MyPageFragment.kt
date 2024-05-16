package edu.skku.map.capstone.view.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.skku.map.capstone.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        setPolicyClickListeners()
        return binding.root
    }

    private fun setPolicyClickListeners() {
        binding.personalInfoAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metal-ocelot-ebc.notion.site/e8f6f619e5944f7289c503be2e87b13e?pvs=4"))
            startActivity(intent)
        }
        binding.serviceAgreement.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metal-ocelot-ebc.notion.site/e9514d03f5d74eba9a6f0ee3e4c13ab5?pvs=4"))
            startActivity(intent)
        }
        binding.placeAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metal-ocelot-ebc.notion.site/7eca22780d9b4d869afc0be5bdca221e?pvs=4"))
            startActivity(intent)
        }

    }
}