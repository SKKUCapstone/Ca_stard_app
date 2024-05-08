package edu.skku.map.capstone

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.KakaoMapSdk
//import edu.skku.map.capstone.databinding.ActivityMainBinding
import edu.skku.map.capstone.databinding.ActivityMainBinding
import edu.skku.map.capstone.dialogs.ReviewDialogCategory
import edu.skku.map.capstone.dialogs.ReviewDialogComment
import edu.skku.map.capstone.dialogs.ReviewDialogRating
import edu.skku.map.capstone.fragments.FavoriteFragment
import edu.skku.map.capstone.fragments.HomeFragment
import edu.skku.map.capstone.fragments.MyCafeFragment
import edu.skku.map.capstone.fragments.MyPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>
    val reviewPhase = MutableLiveData(0)
    private var upperFragment: Fragment? = null
    private lateinit var homeFragment: HomeFragment
    private lateinit var favoriteFragment: FavoriteFragment
    private lateinit var myCafeFragment: MyCafeFragment
    private lateinit var myPageFragment: MyPageFragment

    private var dialogCategory:ReviewDialogCategory? = null
    private var dialogRating:ReviewDialogRating? = null
    private var dialogComment:ReviewDialogComment? = null


    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        KakaoMapSdk.init(this, "09e7ce580fee2dc13ec5d24c66cd8238")
        setActivityResultLauncher()
        resolvePermission(permissions)
        setNavActions()
        setUI()
        observeReviewPhase()

    }
    private fun setUI(){
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, homeFragment).commit()
    }

    private fun removeUpperFragment() {
        if(upperFragment != null) {
            supportFragmentManager.beginTransaction().apply {
                remove(upperFragment as Fragment).commit()
            }
        }
        upperFragment = null
    }
    private fun setNavActions() {
        homeFragment = HomeFragment()
        favoriteFragment = FavoriteFragment()
        myCafeFragment = MyCafeFragment()
        myPageFragment = MyPageFragment()

        binding.homeBtn.setOnClickListener {
            removeUpperFragment()
        }

        binding.favBtn.setOnClickListener {
            removeUpperFragment()
            if ( upperFragment == favoriteFragment ) return@setOnClickListener
            supportFragmentManager.beginTransaction().apply {
                add(binding.frameLayout.id, favoriteFragment).commit()
            }
            upperFragment = favoriteFragment
        }

        binding.myCafeBtn.setOnClickListener {
            removeUpperFragment()
            if ( upperFragment == myCafeFragment ) return@setOnClickListener
            supportFragmentManager.beginTransaction().apply {
                add(binding.frameLayout.id, myCafeFragment).commit()
            }
            upperFragment = myCafeFragment
        }

        binding.myPageBtn.setOnClickListener {
            removeUpperFragment()
            if ( upperFragment == myPageFragment ) return@setOnClickListener
            supportFragmentManager.beginTransaction().apply {
                add(binding.frameLayout.id, myPageFragment).commit()
            }
            upperFragment = myPageFragment
        }
    }
        private fun setActivityResultLauncher() {
            activityResultLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
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
            Log.d("permission", res.toString())
            if (!res) {
                activityResultLauncher.launch(permissions)
            }
        }
        private fun createReviewInstance() {
            dialogCategory = ReviewDialogCategory(this, this, reviewPhase)
            dialogRating = ReviewDialogRating(this,this, reviewPhase)
            dialogComment = ReviewDialogComment(this,this, reviewPhase)
        }

        private fun observeReviewPhase() {
            reviewPhase.observe(this as LifecycleOwner) {
                Log.d("dialog","phase is $it")
                if(it == 0) {
                    Log.d("dialog","phase : 0")
                    dialogCategory?.dismiss()
                    dialogRating?.dismiss()
                    dialogComment?.dismiss()
                    dialogRating = null
                    dialogComment = null
                    dialogCategory = null
                }
                if(it == 1) {
                    createReviewInstance()
                    Log.d("dialog","phase : 1")
//                    dialogCategory!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    dialogCategory!!.show()
                }
                if(it == 2) {
                    Log.d("dialog","phase : 2")
//                    dialogRating!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    dialogRating!!.show()
                    dialogCategory!!.dismiss()
                }
                if(it == 3) {
                    Log.d("dialog","phase : 3")
//                    dialogComment!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    dialogComment!!.show()
                    dialogComment!!.dismiss()
                }
            }
        }



}