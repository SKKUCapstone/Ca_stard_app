package edu.skku.map.capstone.view.recommend

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemCafeFavoriteListBinding
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.view.mycafe.minireviewchip.MiniReviewChipListAdapter

class RecommendCafeListViewholder(val context: Context, var binding: ItemCafePreviewBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(cafe: Cafe){
        binding.cafeNameTV.text = cafe.cafeName
//        binding.reviewChipRV.adapter = MiniReviewChipListAdapter(context, cafe.getTopCategories())
    }
}