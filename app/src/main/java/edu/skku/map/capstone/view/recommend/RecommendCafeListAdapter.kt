package edu.skku.map.capstone.view.recommend

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import edu.skku.map.capstone.databinding.ItemCafeFavoriteListBinding
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.manager.CafeDetailManager
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.view.detail.DetailActivity
import edu.skku.map.capstone.view.home.cafelist.CafeListViewholder

class RecommendCafeListAdapter(val context: Context, private var  recommendCafeList: List<Cafe>):RecyclerView.Adapter<RecommendCafeListViewholder>() {
    @SuppressLint("NotifyDataSetChanged")
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
        val cafe = recommendCafeList[position]
        holder.bind(cafe)

        holder.binding.previewBodyV.setOnClickListener {
            CafeDetailManager.getInstance().viewCafe(cafe)
            context.startActivity(Intent(context,DetailActivity::class.java))
        }
    }

    private fun initFlexboxLayout(rv: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        rv.layoutManager = flexboxLayoutManager
    }


}