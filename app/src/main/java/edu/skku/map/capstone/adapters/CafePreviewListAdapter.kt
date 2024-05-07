package edu.skku.map.capstone.adapters

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
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewholders.CafePreviewListViewholder

class CafePreviewListAdapter(val context: Context, val onCafeClick: MutableLiveData<Cafe>): RecyclerView.Adapter<CafePreviewListViewholder>() {
    private var cafeList: List<Cafe> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCafeList(newList: List<Cafe>) {
        cafeList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafePreviewListViewholder {
        val binding = ItemCafePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

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
        holder.binding.previewBodyCL.setOnClickListener {
            onCafeClick.postValue(cafe)
        }
    }
}