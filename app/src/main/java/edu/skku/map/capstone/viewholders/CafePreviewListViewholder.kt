package edu.skku.map.capstone.viewholders

import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.models.Review

class CafePreviewListViewholder(var binding:ItemCafePreviewBinding):RecyclerView.ViewHolder(binding.root) {
    //데이터를 View에 직접 연결해서 구성해주는 컴포넌트
    fun bind(cafe: Cafe){
        binding.cafeNameTV.text = cafe.cafeName
        binding.distanceTV.text = "${cafe.distance?.toInt()}KM"
        binding.ratingTV.text = getAverage(cafe.reviews).toString()

    }

    private fun getAverage(reviews: ArrayList<Review>): Double {
        var total = 0.0
        for(review in reviews){
            total += review.total
        }
        if(reviews.isNotEmpty()) total /= reviews.size
        return total
    }
}