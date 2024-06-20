package edu.skku.map.capstone.view.mycafe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
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

class FavoriteListAdapter(val context: Context): RecyclerView.Adapter<FavoriteListViewholder>() {
    private var cafeList: List<Cafe> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCafeList(newList: List<Cafe>) {
        cafeList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewholder {
        val binding = ItemCafeFavoriteListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_END
        binding.minireviewChipRV.layoutManager = flexboxLayoutManager
        return FavoriteListViewholder(context, binding)
    }
    override fun getItemCount(): Int {
        return cafeList.size
    }
    override fun onBindViewHolder(holder: FavoriteListViewholder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe)
        holder.binding.cafeItemLL.setOnClickListener {
            CafeDetailManager.getInstance().viewCafe(cafe)
            context.startActivity(Intent(context, DetailActivity::class.java))
        }
    }
}

