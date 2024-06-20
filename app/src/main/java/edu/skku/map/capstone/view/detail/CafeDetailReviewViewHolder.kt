package edu.skku.map.capstone.view.detail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemReviewDetailBinding
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.view.myreviews.reviewchip.ReviewedChipListAdapter

class CafeDetailReviewViewHolder(val context: Context, var binding: ItemReviewDetailBinding):RecyclerView.ViewHolder(binding.root) {
    //데이터를 View에 직접 연결해서 구성해주는 컴포넌트

    fun bind(review: Review){
        binding.reviewUserName.text = if(review.userName == "null") "(알 수 없음)" else review.userName
        //TODO: timestamp
        binding.reviewDate.text = "2023.12.10"
        binding.commentTV.text = review.comment
        binding.miniReviewChipRV.adapter = ReviewedChipListAdapter(context, review.abstractReview())
    }
}