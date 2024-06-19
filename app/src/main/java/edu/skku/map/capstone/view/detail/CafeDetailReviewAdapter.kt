package edu.skku.map.capstone.view.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemReviewDetailBinding
import edu.skku.map.capstone.models.review.Review

class CafeDetailReviewAdapter(val context: Context): RecyclerView.Adapter<CafeDetailReviewViewHolder>() {
    private var reviewList: List<Review> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCafeList(newList: List<Review>) {
        reviewList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeDetailReviewViewHolder {
        val binding = ItemReviewDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        val flexboxLayoutManager = FlexboxLayoutManager(context)
//        flexboxLayoutManager.flexDirection = FlexDirection.ROW
//        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
//        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_END
//        binding.minireviewChipRV.layoutManager = flexboxLayoutManager
        return CafeDetailReviewViewHolder(context, binding)
    }
    override fun getItemCount(): Int {
        return reviewList.size
    }
    override fun onBindViewHolder(holder: CafeDetailReviewViewHolder, position: Int) {
        val cafe = reviewList[position]
        holder.bind(cafe)
//        holder.binding.previewBodyCL.setOnClickListener {
//            onCafeClick.postValue(cafe)
//        }
    }
}