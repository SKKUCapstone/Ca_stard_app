package edu.skku.map.capstone.view.recommend

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.databinding.ItemCafeFavoriteListBinding
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.manager.MyLocationManager
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.util.getCafeDistance
import edu.skku.map.capstone.view.home.cafelist.reviewchip.ReviewChipListAdapter
import edu.skku.map.capstone.view.mycafe.minireviewchip.MiniReviewChipListAdapter

class RecommendCafeListViewholder(val context: Context, var binding: ItemCafePreviewBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(cafe: Cafe){
        val reviewChipListAdapter = ReviewChipListAdapter(context, cafe.filterTopReviews())
        Log.d("cafelistviewholder", "Top Attributes: ${cafe.filterTopReviews()}")
        binding.cafeNameTV.text = cafe.cafeName
        binding.distanceTV.text = "${getCafeDistance(MyLocationManager.getInstance().latLng.value!!, LatLng.from(cafe.latitude,cafe.longitude))}m"
        binding.ratingTV.text = if(cafe.getTotalCnt() == 0) "별점 정보 없음" else cafe.getTotalRating().toString()
        binding.reviewChipRV.adapter = reviewChipListAdapter
    }
}