package edu.skku.map.capstone.view.home.cafelist

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
import edu.skku.map.capstone.models.cafe.Cafe

class CafeListAdapter(val context: Context, private val onCafeClick: MutableLiveData<Cafe>): RecyclerView.Adapter<CafeListViewholder>() {
    private var cafeList: List<Cafe> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCafeList(newList: List<Cafe>) {
        cafeList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeListViewholder {
        val binding = ItemCafePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        initFlexboxLayout(binding.reviewChipRV)
        return CafeListViewholder(context, binding)
    }
    override fun getItemCount(): Int {
        return cafeList.size
    }
    override fun onBindViewHolder(holder: CafeListViewholder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe)
        holder.binding.previewBodyCL.setOnClickListener {
            onCafeClick.postValue(cafe)
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

