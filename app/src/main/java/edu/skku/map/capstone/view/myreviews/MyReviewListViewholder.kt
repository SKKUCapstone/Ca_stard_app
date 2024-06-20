package edu.skku.map.capstone.view.myreviews
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemReviewBinding
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.view.dialog.review.ReviewViewModel
import edu.skku.map.capstone.view.myreviews.reviewchip.ReviewedChipListAdapter

class MyReviewListViewholder(val context: Context, var binding: ItemReviewBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(review: Review){
        binding.cafeNameTV.text = review.cafeName
        binding.commentTV.text = review.comment
        binding.miniReviewChipRV.adapter = ReviewedChipListAdapter(context, review.abstractReview())
    }
}