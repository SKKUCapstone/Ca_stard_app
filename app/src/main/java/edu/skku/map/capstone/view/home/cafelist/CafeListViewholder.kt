package edu.skku.map.capstone.view.home.cafelist

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.kakao.vectormap.LatLng
import edu.skku.map.capstone.view.home.cafelist.reviewchip.ReviewChipListAdapter
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.getCafeDistance

class CafeListViewholder(val context: Context, var binding:ItemCafePreviewBinding):RecyclerView.ViewHolder(binding.root) {
    //데이터를 View에 직접 연결해서 구성해주는 컴포넌트

    fun bind(cafe: Cafe){
        val reviewChipListAdapter = ReviewChipListAdapter(context, cafe.getTopCategories())
        Log.d("cafelistviewholder", "Top Attributes: ${cafe.getTopCategories()}")
        binding.cafeNameTV.text = cafe.cafeName
        binding.distanceTV.text = "${getCafeDistance(User.getInstance().latLng.value!!, LatLng.from(cafe.latitude,cafe.longitude))}km"
        binding.ratingTV.text = if(cafe.getTotalCnt() == 0) "별점 정보 없음" else cafe.getTotalRating().toString()
        binding.reviewChipRV.adapter = reviewChipListAdapter
    }
}