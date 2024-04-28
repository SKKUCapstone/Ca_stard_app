package edu.skku.map.capstone

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kakao.vectormap.KakaoMapSdk
import edu.skku.map.capstone.databinding.ActivityMainBinding
import edu.skku.map.capstone.fragments.FavoriteFragment
import edu.skku.map.capstone.fragments.HomeFragment
import edu.skku.map.capstone.fragments.MyCafeFragment
import edu.skku.map.capstone.fragments.MyPageFragment
import edu.skku.map.capstone.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>


    private val viewModel = MainViewModel()
    private lateinit var homeFragment: HomeFragment
    private lateinit var favoriteFragment: FavoriteFragment
    private lateinit var myCafeFragment: MyCafeFragment
    private lateinit var myPageFragment: MyPageFragment


    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        KakaoMapSdk.init(this, "09e7ce580fee2dc13ec5d24c66cd8238")
        viewModel.fetchData()
        setActivityResultLauncher()
        resolvePermission(permissions)

        setNavActions()
        setUI()
    }
    private fun setUI(){
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, homeFragment).commit()

    }
    private fun setNavActions() {
        homeFragment = HomeFragment()
        favoriteFragment = FavoriteFragment()
        myPageFragment = MyPageFragment()
        myCafeFragment = MyCafeFragment()

        binding.homeBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, homeFragment).commit()
        }
        binding.favBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, favoriteFragment).commit()
        }
        binding.myCafeBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, myCafeFragment).commit()
        }
        binding.myPageBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, myPageFragment).commit()
        }
    }
    private fun setActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { permission -> permission.value }) {
                return@registerForActivityResult
            } else {
                Toast.makeText(this, "권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun resolvePermission(permissions: Array<String>) {
        val res = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        Log.d("permission",res.toString())
        if(!res){
            activityResultLauncher.launch(permissions)
        }
    }
}