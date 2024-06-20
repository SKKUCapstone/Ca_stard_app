package edu.skku.map.capstone.view.mycafe.minireviewchip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import edu.skku.map.capstone.databinding.ItemMiniCategorychipBinding

class MiniReviewChipListAdapter(val context: Context, private val reviewList: List<String>):
    RecyclerView.Adapter<MiniReviewChipListViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniReviewChipListViewholder {
        val binding = ItemMiniCategorychipBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MiniReviewChipListViewholder(context, binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: MiniReviewChipListViewholder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }
}