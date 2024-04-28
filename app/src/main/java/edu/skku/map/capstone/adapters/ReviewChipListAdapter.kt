package edu.skku.map.capstone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemReviewChipBinding
import edu.skku.map.capstone.viewholders.ReviewChipListViewholder

class ReviewChipListAdapter(val context: Context, private val reviewList: ArrayList<String>):RecyclerView.Adapter<ReviewChipListViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewChipListViewholder {
        val binding = ItemReviewChipBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewChipListViewholder(context ,binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewChipListViewholder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }
}