package edu.skku.map.capstone

import android.content.Intent
import android.content.ContentValues.TAG
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
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.kakao.vectormap.KakaoMapSdk
import edu.skku.map.capstone.databinding.ActivityMainBinding
import edu.skku.map.capstone.view.dialog.review.category.ReviewDialogCategory
import edu.skku.map.capstone.view.dialog.review.comment.ReviewDialogComment
import edu.skku.map.capstone.view.dialog.review.rating.ReviewDialogRating
import edu.skku.map.capstone.view.favorite.FavoriteFragment
import edu.skku.map.capstone.view.home.HomeFragment
import edu.skku.map.capstone.view.mycafe.MyCafeFragment
import edu.skku.map.capstone.view.mypage.MyPageFragment
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.view.dialog.review.ReviewViewModel
import edu.skku.map.capstone.view.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>
    private var upperFragment: Fragment? = null
    private lateinit var homeFragment: HomeFragment
    private lateinit var favoriteFragment: FavoriteFragment
    private lateinit var myCafeFragment: MyCafeFragment
    private lateinit var myPageFragment: MyPageFragment

    private var dialogCategory: ReviewDialogCategory? = null
    private var dialogRating: ReviewDialogRating? = null
    private var dialogComment: ReviewDialogComment? = null

    var favoriteCafeList = MutableLiveData<ArrayList<Cafe>>(arrayListOf<Cafe>())
    private var reviewViewModel: ReviewViewModel? = null

    var reviewingCafe = MutableLiveData<Cafe>(null)
    val reviewPhase = MutableLiveData(0)

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkLoginStatus()
        KakaoMapSdk.init(this, "09e7ce580fee2dc13ec5d24c66cd8238")
        setActivityResultLauncher()
        resolvePermission(permissions)
        setNavActions()
        setUI()
        observeReviewPhase()
    }
    private fun setUI(){
        supportActionBar?.hide()
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, homeFragment).commit()
        binding.homeIcon.isActivated = true
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
            binding.homeIcon.isActivated = true
            binding.favIcon.isActivated = false
            binding.myCafeIcon.isActivated = false
            binding.myPageIcon.isActivated = false
        }

        binding.favBtn.setOnClickListener {
            removeUpperFragment()
            if ( upperFragment == favoriteFragment ) return@setOnClickListener
            supportFragmentManager.beginTransaction().apply {
                add(binding.frameLayout.id, favoriteFragment).commit()
            }
            upperFragment = favoriteFragment
            binding.homeIcon.isActivated = false
            binding.favIcon.isActivated = true
            binding.myCafeIcon.isActivated = false
            binding.myPageIcon.isActivated = false
        }

        binding.myCafeBtn.setOnClickListener {
            removeUpperFragment()
            if ( upperFragment == myCafeFragment ) return@setOnClickListener
            supportFragmentManager.beginTransaction().apply {
                add(binding.frameLayout.id, myCafeFragment).commit()
            }
            upperFragment = myCafeFragment
            binding.homeIcon.isActivated = false
            binding.favIcon.isActivated = false
            binding.myCafeIcon.isActivated = true
            binding.myPageIcon.isActivated = false
        }

        binding.myPageBtn.setOnClickListener {
            removeUpperFragment()
            if ( upperFragment == myPageFragment ) return@setOnClickListener
            supportFragmentManager.beginTransaction().apply {
                add(binding.frameLayout.id, myPageFragment).commit()
            }
            upperFragment = myPageFragment
            binding.homeIcon.isActivated = false
            binding.favIcon.isActivated = false
            binding.myCafeIcon.isActivated = false
            binding.myPageIcon.isActivated = true
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
        private fun initReviewViewModel() {
            reviewViewModel = ReviewViewModel(this, reviewingCafe.value!!)
        }

        private fun initCategoryDialog() {
            dialogCategory = ReviewDialogCategory(reviewViewModel!!,this,  reviewPhase)
        }

        private fun initRatingDialog() {
            dialogRating = ReviewDialogRating(reviewViewModel!!,this, reviewPhase)
        }

        private fun initCommentDialog() {
            dialogComment = ReviewDialogComment(reviewViewModel!!,this, reviewPhase)
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
                    reviewingCafe.postValue(null)
                }
                if(it == 1) {
                    initReviewViewModel()
                    initCategoryDialog()
                    Log.d("dialog","phase : 1")
                    dialogCategory!!.show()
                }
                if(it == 2) {
                    Log.d("dialog","phase : 2")
                    initRatingDialog()
                    dialogRating!!.show()
                    dialogCategory!!.dismiss()
                }
                if(it == 3) {
                    Log.d("dialog","phase : 3")
                    initCommentDialog()
                    dialogComment!!.show()
                    dialogRating!!.dismiss()
                }
            }
        }

    private fun checkLoginStatus() {
        if(AuthApiClient.instance.hasToken()) {
            // 로그인 정보 확인
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                // 로그인 필요
                if (error != null) {
                    Toast.makeText(this, "토큰 실패", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
                // 토큰 유효성 체크 성공 (필요 시 토큰 갱신됨)
                else if (tokenInfo != null) {
                    // 백엔드 Login Post

                    Toast.makeText(this, "토큰 성공", Toast.LENGTH_SHORT).show()

                }
            }
        }
        // 로그인 필요
            else {
                //  화면 넘어가기
                Log.e(TAG, "카카오톡으로 로그인 실패")

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
    }
}