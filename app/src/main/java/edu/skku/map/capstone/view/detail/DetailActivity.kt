package edu.skku.map.capstone.view.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ActivityDetailBinding
import edu.skku.map.capstone.manager.CafeDetailManager
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.getCafeDistance

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var cafe: Cafe
    private lateinit var cafeDetailReviewListAdapter: CafeDetailReviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(CafeDetailManager.getInstance().currentViewingCafe.value == null) finish()
        cafe = CafeDetailManager.getInstance().currentViewingCafe.value!!
        setUI()
        setClickListeners()
    }

    private fun setUI() {
        binding.detailCafeNameTV.text = cafe.cafeName
        binding.detailCafeName2TV.text = cafe.cafeName
        binding.detailAddressTV.text = if(cafe.roadAddressName == "") "정보 없음" else cafe.roadAddressName
        binding.detailUrlTV.text = if(cafe.placeURL == null) "웹사이트 정보 없음" else cafe.placeURL.toString()
        binding.detailPhoneTV.text = if(cafe.phone == "") "정보 없음" else cafe.phone
        binding.detailDistanceTV.text = getCafeDistance(User.latLng.value!!, LatLng.from(cafe.latitude, cafe.longitude)) +"m"
        binding.detailRatingTV.text = if(cafe.getTotalRating() == null) "별점 정보 없음" else cafe.getTotalRating().toString()

        val ratingLLs = listOf(
            binding.ratingCapacityLL,
            binding.ratingBrightLL,
            binding.ratingCleanLL,
            binding.ratingWifiLL,
            binding.ratingQuietLL,
            binding.ratingTablesLL,
            binding.ratingPowerSocketLL,
            binding.ratingToiletLL
        )

        val ratingCVs = listOf(
            binding.ratingbarCapacityScoreCV,
            binding.ratingbarBrightScoreCV,
            binding.ratingbarCleanScoreCV,
            binding.ratingbarWifiScoreCV,
            binding.ratingbarQuietScoreCV,
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

        if(cafe.getTotalCnt() == 0) {
            binding.ratingListRV.visibility = View.INVISIBLE
            binding.noDataView.visibility = View.VISIBLE
        }
        for(i in 0..7) {
            //rating bar is not visible if there is no review.
            if(ratingCnts[i] == 0) ratingLLs[i].visibility = View.GONE

            //style of each ratingbar
            ratingTVs[i].text = ratings[i].toString()
            val layoutParams = ratingCVs[i].layoutParams as ViewGroup.LayoutParams
            layoutParams.width = ratingBarLength(ratings[i])
            ratingCVs[i].layoutParams = layoutParams
        }

        // Review List
        cafeDetailReviewListAdapter = CafeDetailReviewAdapter(this)
        binding.reviewListRV.adapter = cafeDetailReviewListAdapter
        cafeDetailReviewListAdapter.updateCafeList(cafe.reviews)

        //Cafe image
        val cafeImage:Int
        if(cafe.cafeName!!.startsWith("스타벅스")) cafeImage = R.drawable.starbucks
        else if(cafe.cafeName!!.startsWith("투썸플레이스")) cafeImage = R.drawable.twosome
        //add more cafe images..
        else cafeImage = R.drawable.defaultcafe1
        binding.detailCafeIV.setImageResource(cafeImage)
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
//        binding.detailReviewBtn.setOnClickListener {
//            reviewingCafe.postValue(cafe)
//            phase.postValue(1)
//        }
//        binding.detailReviewBtn2.setOnClickListener {
//            reviewingCafe.postValue(cafe)
//            phase.postValue(1)
//        }
//
//        binding.detailMapBtn.setOnClickListener {
//            pullDownBottomSheet.postValue(true)
//        }
//        binding.detailFavBtn.setOnClickListener {
//            val res = viewModel.onAddFavorite(cafe.cafeId)
//            cafe.updateIsFavorite(res) //synchronize data
//        }

        binding.detailURLBtn.setOnClickListener {
            // Intent를 사용하여 웹 브라우저 열기
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(cafe.placeURL))
            startActivity(browserIntent)
        }
    }

    private fun ratingBarLength(rating:Double):Int = (rating/5.0*578).toInt()

}