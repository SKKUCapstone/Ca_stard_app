package edu.skku.map.capstone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewholders.CafePreviewListViewholder

class CafePreviewListAdapter(val context: Context, val cafeList: ArrayList<Cafe>):RecyclerView.Adapter<CafePreviewListViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafePreviewListViewholder {
        val binding = ItemCafePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CafePreviewListViewholder(binding)
    }

    override fun getItemCount(): Int {
        return cafeList.size
    }

    override fun onBindViewHolder(holder: CafePreviewListViewholder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe)
    }
}