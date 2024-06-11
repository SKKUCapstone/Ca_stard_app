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
        binding.reviewChipRV.adapter = ReviewedChipListAdapter(context, renderReviews(review))

    }
    private fun renderReviews(review: Review):ArrayList<Pair<String,Int>> {
        val pairs = arrayListOf<Pair<String,Int>>()
        if(review.capacity != 0) {
            pairs.add(Pair("capacity", review.capacity))
        }
        if(review.quiet != 0) {
            pairs.add(Pair("quiet", review.quiet))
        }
        if(review.toilet != 0) {
            pairs.add(Pair("toilet", review.toilet))
        }
        if(review.wifi != 0) {
            pairs.add(Pair("wiif", review.wifi))
        }
        if(review.bright != 0) {
            pairs.add(Pair("bright", review.bright))
        }
        if(review.clean != 0) {
            pairs.add(Pair("clean", review.clean))
        }
        if(review.powerSocket != 0) {
            pairs.add(Pair("powerSocket", review.powerSocket))
        }
        if(review.tables != 0) {
            pairs.add(Pair("tables", review.toilet))
        }
        return pairs
    }

}