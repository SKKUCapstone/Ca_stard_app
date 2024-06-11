package edu.skku.map.capstone.view.myreviews.reviewchip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemMiniCategorychipBinding

class ReviewedChipListAdapter(val context: Context, private val reviewList: ArrayList<Pair<String,Int>>):RecyclerView.Adapter<ReviewedChipListViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewedChipListViewholder {
        val binding = ItemMiniCategorychipBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewedChipListViewholder(context, binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewedChipListViewholder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }
}