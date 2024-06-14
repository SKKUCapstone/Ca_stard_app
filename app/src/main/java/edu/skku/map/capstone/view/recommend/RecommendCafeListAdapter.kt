package edu.skku.map.capstone.view.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import edu.skku.map.capstone.databinding.ItemCafeFavoriteListBinding
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.cafe.Cafe

class RecommendCafeListAdapter(val context: Context, private var  recommendCafeList: List<Cafe>):RecyclerView.Adapter<RecommendCafeListViewholder>() {
    fun updateCafes(newCafeList: List<Cafe>) {
        this.recommendCafeList = newCafeList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCafeListViewholder {
        val binding = ItemCafePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        initFlexboxLayout(binding.reviewChipRV)
        return RecommendCafeListViewholder(context, binding)
    }

    override fun getItemCount(): Int {
        return recommendCafeList.size
    }

    override fun onBindViewHolder(holder: RecommendCafeListViewholder, position: Int) {
        val review = recommendCafeList[position]
        holder.bind(review)
    }

    private fun initFlexboxLayout(rv: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        rv.layoutManager = flexboxLayoutManager
    }
}