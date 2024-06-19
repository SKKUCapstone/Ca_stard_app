package edu.skku.map.capstone.view.detail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemReviewDetailBinding
import edu.skku.map.capstone.models.review.Review

class CafeDetailReviewViewHolder(val context: Context, var binding: ItemReviewDetailBinding):RecyclerView.ViewHolder(binding.root) {
    //데이터를 View에 직접 연결해서 구성해주는 컴포넌트

    fun bind(review: Review){
        // reviewID 토대로 review 이름 불러와야함
        binding.reviewUserName.text = "정환"
        // timestamp 저장 로직 필요
        binding.reviewDate.text = "2023.12.10"

//        Log.d("favlistviewholder", "Cafe: ${cafe.cafeName}, Top Attributes: ${cafe.filterTop()}")
        //        setClickListener()
    }
}