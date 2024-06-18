package edu.skku.map.capstone.view.myreviews

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import edu.skku.map.capstone.databinding.ItemReviewBinding
import edu.skku.map.capstone.manager.MyReviewManager
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User

class MyReviewListAdapter(val context: Context): RecyclerView.Adapter<MyReviewListViewholder>() {
    private var reviewList = ArrayList<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewListViewholder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        initFlexboxLayout(binding.miniReviewChipRV)
        return MyReviewListViewholder(context, binding)
    }
    override fun getItemCount(): Int {
        Log.d("@@@myreview", "size: ${reviewList.size}")
        return reviewList.size
    }
    override fun onBindViewHolder(holder: MyReviewListViewholder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
        holder.binding.deleteBtn.setOnClickListener {
            Log.d("@@@myreview", "delete request on user ${User.getInstance().id} and review ${review.reviewId}")

            MyReviewManager.getInstance().onDeleteReview(review.reviewId)
        }
    }
    private fun initFlexboxLayout(rv: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        rv.layoutManager = flexboxLayoutManager
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(reviewList: ArrayList<Review>) {
        this.reviewList = reviewList
        this.notifyDataSetChanged()
    }

    private fun onDeleteReview(reviewId: Long) {
        MyReviewManager.getInstance().onDeleteReview(reviewId)
    }


}