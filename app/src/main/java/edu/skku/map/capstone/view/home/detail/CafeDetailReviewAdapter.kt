package edu.skku.map.capstone.view.home.detail

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
import edu.skku.map.capstone.databinding.ItemCafeFavoriteListBinding
import edu.skku.map.capstone.databinding.ItemReviewDetailBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review
import edu.skku.map.capstone.view.mycafe.FavoriteListViewholder

class CafeDetailReviewAdapter(val context: Context, private val onCafeClick: MutableLiveData<Review>): RecyclerView.Adapter<CafeDeatilReviewViewHolder>() {
    private var reviewList: List<Review> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCafeList(newList: List<Review>) {
        reviewList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeDeatilReviewViewHolder {
        val binding = ItemReviewDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        val flexboxLayoutManager = FlexboxLayoutManager(context)
//        flexboxLayoutManager.flexDirection = FlexDirection.ROW
//        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
//        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_END
//        binding.minireviewChipRV.layoutManager = flexboxLayoutManager
        return CafeDeatilReviewViewHolder(context, binding)
    }
    override fun getItemCount(): Int {
        return reviewList.size
    }
    override fun onBindViewHolder(holder: CafeDeatilReviewViewHolder, position: Int) {
        val cafe = reviewList[position]
        holder.bind(cafe)
//        holder.binding.previewBodyCL.setOnClickListener {
//            onCafeClick.postValue(cafe)
//        }
    }
}