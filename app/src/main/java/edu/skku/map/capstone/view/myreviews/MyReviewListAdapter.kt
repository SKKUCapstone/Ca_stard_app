package edu.skku.map.capstone.view.myreviews

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.databinding.ItemReviewBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.view.mycafe.FavoriteListViewholder

class MyReviewListAdapter(val context: Context, val reviewList:List<Review>): RecyclerView.Adapter<MyReviewListViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewListViewholder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MyReviewListViewholder(context, binding)
    }
    override fun getItemCount(): Int {
        return reviewList.size
    }
    override fun onBindViewHolder(holder: MyReviewListViewholder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }
}