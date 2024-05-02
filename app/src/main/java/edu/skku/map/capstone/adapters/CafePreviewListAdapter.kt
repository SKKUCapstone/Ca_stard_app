package edu.skku.map.capstone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewholders.CafePreviewListViewholder

class CafePreviewListAdapter(val context: Context, private val cafeList: List<Cafe>):RecyclerView.Adapter<CafePreviewListViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafePreviewListViewholder {
        val binding = ItemCafePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

//        FlexboxLayoutManager(context).apply {
//            flexWrap = FlexWrap.WRAP
//            flexDirection = FlexDirection.ROW
//            justifyContent = JustifyContent.CENTER
//        }.let{
//            binding.reviewChipRV.layoutManager = it
//        }
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.reviewChipRV.layoutManager = flexboxLayoutManager

        return CafePreviewListViewholder(context, binding)
    }

    override fun getItemCount(): Int {
        return cafeList.size
    }

    override fun onBindViewHolder(holder: CafePreviewListViewholder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe)
    }


}