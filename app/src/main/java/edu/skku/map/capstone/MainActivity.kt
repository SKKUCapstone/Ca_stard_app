package edu.skku.map.capstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMapSdk
import edu.skku.map.capstone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val viewModel = MainViewModel()
    private lateinit var FavoriteFragment: FavoriteFragment
    private lateinit var HomeFragment: HomeFragment
    private lateinit var MyCafeFragment: MyCafeFragment
    private lateinit var MyPageFragment: MyPageFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        KakaoMapSdk.init(this, "09e7ce580fee2dc13ec5d24c66cd8238")

        viewModel.fetchData()




        navActions()
        setUI()
    }
    private fun setUI(){
        HomeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, HomeFragment).commit()

    }
    private fun navActions() {
        HomeFragment = HomeFragment()
        FavoriteFragment = FavoriteFragment()
        MyPageFragment = MyPageFragment()
        MyCafeFragment = MyCafeFragment()

        binding.homeBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, HomeFragment).commit()
        }
        binding.favBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, FavoriteFragment).commit()
        }
        binding.myCafeBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, MyCafeFragment).commit()
        }
        binding.myPageBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, MyPageFragment).commit()
        }
    }
}