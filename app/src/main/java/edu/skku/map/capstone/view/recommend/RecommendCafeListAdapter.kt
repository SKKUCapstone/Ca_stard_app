package edu.skku.map.capstone.view.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemCafeFavoriteListBinding
import edu.skku.map.capstone.models.cafe.Cafe

class RecommendCafeListAdapter(val context: Context, private val reviewList: List<Cafe>):RecyclerView.Adapter<RecommendCafeListViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCafeListViewholder {
        val binding = ItemCafeFavoriteListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecommendCafeListViewholder(context, binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: RecommendCafeListViewholder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }
}