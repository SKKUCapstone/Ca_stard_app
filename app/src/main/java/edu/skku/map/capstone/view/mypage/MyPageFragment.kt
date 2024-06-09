package edu.skku.map.capstone.view.mypage

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kakao.sdk.user.UserApiClient
import edu.skku.map.capstone.databinding.FragmentMyPageBinding
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.view.login.LoginActivity
import edu.skku.map.capstone.view.myreviews.MyReviewsActivity
import kotlinx.coroutines.Dispatchers.Main

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        setUI()
        setPolicyClickListeners()
        return binding.root
    }

    private fun setUI(){
        binding.userName.text = User.username
        binding.userEmail.text = User.email
    }
    private fun setPolicyClickListeners() {
        binding.personalInfoAgreementCV.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metal-ocelot-ebc.notion.site/e8f6f619e5944f7289c503be2e87b13e?pvs=4"))
            startActivity(intent)
        }
        binding.serviceAgreementCV.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metal-ocelot-ebc.notion.site/e9514d03f5d74eba9a6f0ee3e4c13ab5?pvs=4"))
            startActivity(intent)
        }
        binding.placeAgreementCV.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metal-ocelot-ebc.notion.site/7eca22780d9b4d869afc0be5bdca221e?pvs=4"))
            startActivity(intent)
        }

        binding.logoutCV.setOnClickListener {
            logOut()
        }

        binding.myReviewsCV.setOnClickListener {
            startActivity(Intent(requireActivity(), MyReviewsActivity::class.java))

        }
    }

    private fun logOut() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(ContentValues.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
        val intent = Intent (activity, LoginActivity::class.java)
        activity?.startActivity(intent)
    }

}