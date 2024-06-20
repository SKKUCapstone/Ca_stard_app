package edu.skku.map.capstone.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ActivityDetailBinding
import edu.skku.map.capstone.manager.CafeDetailManager
import edu.skku.map.capstone.manager.MyReviewManager
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.FavoriteDTO
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.util.getCafeDistance
import edu.skku.map.capstone.view.dialog.review.ReviewViewModel
import edu.skku.map.capstone.view.dialog.review.category.ReviewDialogCategory
import edu.skku.map.capstone.view.dialog.review.comment.ReviewDialogComment
import edu.skku.map.capstone.view.dialog.review.rating.ReviewDialogRating
import edu.skku.map.capstone.view.home.HomeViewModel
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val reviews = MutableLiveData<ArrayList<Review>>()
    private lateinit var cafeDetailReviewListAdapter: CafeDetailReviewAdapter
    private var dialogCategory: ReviewDialogCategory? = null
    private var dialogRating: ReviewDialogRating? = null
    private var dialogComment: ReviewDialogComment? = null
    private var reviewViewModel: ReviewViewModel? = null
    private val reviewPhase = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        reviews.postValue(CafeDetailManager.getInstance().currentViewingCafe.value!!.reviews)
        setUI()
        setClickListeners()
        observeReviewPhase()
        observeReviewUpdate()
        observeFetchReview()
    }

    private fun setUI() {
        val cafe = CafeDetailManager.getInstance().currentViewingCafe.value!!
        supportActionBar?.hide()
        binding.detailCafeNameTV.text = cafe.cafeName
        binding.detailCafeName2TV.text = cafe.cafeName
        binding.detailAddressTV.text = if(cafe.roadAddressName == "") "정보 없음" else cafe.roadAddressName
        binding.detailUrlTV.text = if(cafe.placeURL == null) "웹사이트 정보 없음" else cafe.placeURL
        binding.detailPhoneTV.text = if(cafe.phone == "") "정보 없음" else cafe.phone
        binding.detailDistanceTV.text = getCafeDistance(User.getInstance().latLng.value!!, LatLng.from(cafe.latitude, cafe.longitude)) +"m"

        // 즐겨찾기
        cafe.isFavorite.observe(this) { isFavorite ->
            val favoriteIcon = if (isFavorite) R.drawable.icon_like_filled else R.drawable.icon_like
            binding.detailFavIconIV.setImageResource(favoriteIcon)
        }
        updateRatings()

        // Review List
        cafeDetailReviewListAdapter = CafeDetailReviewAdapter(this)
        binding.reviewListRV.adapter = cafeDetailReviewListAdapter
        cafeDetailReviewListAdapter.updateCafeList(cafe.reviews)

        //Cafe image
        val cafeImage:Int
        if(cafe.cafeName!!.startsWith("스타벅스")) cafeImage = R.drawable.starbucks
        else if(cafe.cafeName.startsWith("투썸플레이스")) cafeImage = R.drawable.twosome
        //add more cafe images..
        else cafeImage = R.drawable.defaultcafe1
        binding.detailCafeIV.setImageResource(cafeImage)


    }

    private fun updateRatings() {
        val cafe = CafeDetailManager.getInstance().currentViewingCafe.value!!
        binding.detailRatingTV.text = if(cafe.getTotalRating() == null) "별점 정보 없음" else cafe.getTotalRating().toString()

        val ratingCVs = listOf(
            binding.ratingbarCapacityScoreCV,
            binding.ratingbarBrightScoreCV,
            binding.ratingbarCleanScoreCV,
            binding.ratingbarQuietScoreCV,
            binding.ratingbarWifiScoreCV,
            binding.ratingbarTablesScoreCV,
            binding.ratingbarPowersocketScoreCV,
            binding.ratingbarToiletScoreCV
        )

        val ratingTVs = listOf(
            binding.ratingCapacityTV,
            binding.ratingBrightTV,
            binding.ratingCleanTV,
            binding.ratingQuietTV,
            binding.ratingWifiTV,
            binding.ratingTablesTV,
            binding.ratingPowersocketTV,
            binding.ratingToiletTV
        )

        val ratings = listOf(
            cafe.capacity,
            cafe.bright,
            cafe.clean,
            cafe.quiet,
            cafe.wifi,
            cafe.tables,
            cafe.powerSocket,
            cafe.toilet
        )

        val ratingCnts = listOf(
            cafe.capacityCnt,
            cafe.brightCnt,
            cafe.cleanCnt,
            cafe.quietCnt,
            cafe.wifiCnt,
            cafe.tablesCnt,
            cafe.powerSocketCnt,
            cafe.toiletCnt
        )

        val noRatingTVs = listOf(
            binding.capacityNoRatingTV,
            binding.brightNoRatingTV,
            binding.cleanNoRatingTV,
            binding.quietNoRatingTV,
            binding.wifiNoRatingTV,
            binding.tablesNoRatingTV,
            binding.powerSocketNoRatingTV,
            binding.toiletNoRatingTV


        )

        if(cafe.getTotalCnt() == 0) {
            binding.ratingListRV.visibility = View.INVISIBLE
            binding.noDataView.visibility = View.VISIBLE
        }
        for(i in 0..7) {
            //rating bar is not visible if there is no review.
            if(ratingCnts[i] != 0) {
                noRatingTVs[i].visibility = View.GONE
                continue
            }

            //style of each ratingbar
            ratingTVs[i].text = ((ratings[i] * 10).roundToInt().toDouble() / 10).toString()
            val layoutParams = ratingCVs[i].layoutParams as ViewGroup.LayoutParams
            layoutParams.width = ratingBarLength(ratings[i])
            Log.d("cafe","width: ${ratings[i]} -> ${ratingBarLength(ratings[i])}")
            ratingCVs[i].layoutParams = layoutParams
        }
    }

    private fun setClickListeners() {
        val cafe = CafeDetailManager.getInstance().currentViewingCafe.value!!
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.detailMapBtn.setOnClickListener {
            finish()
        }
        binding.detailReviewBtn.setOnClickListener {
            reviewPhase.postValue(1)
        }
        binding.detailReviewBtn2.setOnClickListener {
            reviewPhase.postValue(1)
        }
//

        binding.detailFavBtn.setOnClickListener {
            val isFavorite = cafe.isFavorite.value ?: false  // 현재 즐겨찾기 상태를 가져옴, 기본값은 false
            if(isFavorite) {
                deleteFavorite(cafe.cafeId) { response ->
                    // Todo: 일단은 Response가 뭐든 간에 채워넣음
                    cafe.isFavorite.postValue(false)
                    binding.detailFavIconIV.setImageResource(R.drawable.icon_like) // 채워진 하트 이미지로 변경
                }
            }
            else {
                addFavorite(cafe.cafeId) { response ->
                    // Todo: 일단은 Response가 뭐든 간에 채워넣음
                    cafe.isFavorite.postValue(true)
                    binding.detailFavIconIV.setImageResource(R.drawable.icon_like_filled) // 채워진 하트 이미지로 변경
                }
            }
        }

        binding.detailURLBtn.setOnClickListener {
            // Intent를 사용하여 웹 브라우저 열기
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(cafe.placeURL))
            startActivity(browserIntent)
        }
    }

    private fun ratingBarLength(rating:Double):Int = (rating/5.0*578).toInt()

    private fun initReviewViewModel() {
        reviewViewModel = ReviewViewModel(this, CafeDetailManager.getInstance().currentViewingCafe.value!!)
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

    private fun observeReviewUpdate() {
        MyReviewManager.getInstance().reviews.observe(this) {
            fetchReviews()
        }
    }

    private fun observeFetchReview() {
        reviews.observe(this as LifecycleOwner) {
            updateRatings()
            if(reviews.value != null && ::cafeDetailReviewListAdapter.isInitialized) cafeDetailReviewListAdapter.updateCafeList(reviews.value!!)
        }
    }

    private fun fetchReviews() {
        val cafe = CafeDetailManager.getInstance().currentViewingCafe.value!!
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .getCafeReviews(cafe.cafeId)
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val body = response.body()!!
                    val jsonArray = JSONArray(body.string())
                    val newReviewList = arrayListOf<Review>()
//                    Log.d("@@@review", "reviews: ${jsonArray}")
                    for (i in 0 until jsonArray.length()) {
                        val reviewJsonObject = jsonArray.getJSONObject(i)
                        Log.d("review", reviewJsonObject.toString())
                        val review = Review(reviewJsonObject)
                        newReviewList.add(review)
                    }
                    Log.d(
                        "@@@review",
                        "total ${newReviewList.size} review fetched:" + newReviewList.toString()
                    )
                    val updatedReviews = cafe.reviews
                    updatedReviews.clear()
                    updatedReviews.addAll(newReviewList)
                    reviews.postValue(updatedReviews)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                }

            })
    }

    
    // 즐겨찾기
    private fun addFavorite(cafeId: Long, callback: (Boolean) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .addFavorite(FavoriteDTO(User.getInstance().id, cafeId))
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("favorite", response.body().toString())
                    if (response.isSuccessful) {
                        callback(true)
                        Log.d("즐겨찾기 추가", "정상적으로 추가됨")
                    } else {
                        callback(false)
                        Log.d("즐겨찾기 추가", "에러 발생, err:${response}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("favorite", "failed to add favorite: ${t.localizedMessage}")
                    callback(false)
                }
            })
    }

    private fun deleteFavorite(cafeId: Long, callback: (Boolean) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        service
            .deleteFavorite(User.getInstance().id, cafeId)
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("favorite", response.body().toString())
                    if (response.isSuccessful) {
                        callback(true)
                        Log.d("즐겨찾기 삭제", "정상적으로 삭제")
                    } else {
                        callback(false)
                        Log.d("즐겨찾기 삭제", "에러 발생")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("favorite", "failed to add favorite: ${t.localizedMessage}")
                    callback(false)
                }
            })
    }

}