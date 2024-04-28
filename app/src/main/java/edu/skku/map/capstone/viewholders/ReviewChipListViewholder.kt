package edu.skku.map.capstone.viewholders

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ItemReviewChipBinding

class ReviewChipListViewholder(val context:Context, var binding:ItemReviewChipBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(reviewText: String){

            when(reviewText){
                "bright" -> {
                    binding.reviewChipTV.text ="밝아요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_bright_resize),null,null,null)
                }
                "clean" -> {
                    binding.reviewChipTV.text ="깨끗해요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_clean_resize),null,null,null)
                }
                "quiet" -> {
                    binding.reviewChipTV.text ="조용해요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_bright_resize),null,null,null)
                }
                "capacity" -> {
                    binding.reviewChipTV.text ="넓어요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_bright_resize),null,null,null)
                }
                "powerSocket" -> {
                    binding.reviewChipTV.text ="콘센트가 많아요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_bright_resize),null,null,null)
                }
                "wifi" -> {
                    binding.reviewChipTV.text ="와이파이가 빨라요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_wifi_resize),null,null,null)
                }
                "table" -> {
                    binding.reviewChipTV.text ="책상이 넓어요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_table_resize),null,null,null)
                }
                "toilet" -> {
                    binding.reviewChipTV.text ="화장실이 쾌적해요"
                    binding.reviewChipTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.icon_toilet_resize),null,null,null)
                }
                else-> Unit
            }

    }
}