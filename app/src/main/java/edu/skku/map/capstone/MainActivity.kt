package edu.skku.map.capstone

import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kakao.vectormap.KakaoMapSdk
import edu.skku.map.capstone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>


    val viewModel = MainViewModel()
    private lateinit var FavoriteFragment: FavoriteFragment
    private lateinit var HomeFragment: HomeFragment
    private lateinit var MyCafeFragment: MyCafeFragment
    private lateinit var MyPageFragment: MyPageFragment


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
        HomeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, HomeFragment).commit()

    }
    private fun setNavActions() {

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
    private fun setActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { permission -> permission.value }) {

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