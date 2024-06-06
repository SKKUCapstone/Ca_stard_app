package edu.skku.map.capstone.view.recommend.minireviewchip

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ItemMiniCategorychipBinding

class MiniReviewChipListViewholder(val context: Context, var binding: ItemMiniCategorychipBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(reviewText: String){
        when(reviewText){
            "bright" -> {
                binding.iconIV.setImageResource(R.drawable.icon_bright)
            }
            "clean" -> {
                binding.iconIV.setImageResource(R.drawable.icon_clean)
            }
            "quiet" -> {
                binding.iconIV.setImageResource(R.drawable.icon_quiet)
            }
            "capacity" -> {
                binding.iconIV.setImageResource(R.drawable.icon_capacity)
            }
            "powerSocket" -> {
                binding.iconIV.setImageResource(R.drawable.icon_powersocket)
            }
            "wifi" -> {
                binding.iconIV.setImageResource(R.drawable.icon_wifi)
            }
            "tables" -> {
                binding.iconIV.setImageResource(R.drawable.icon_tables)
            }
            "toilet" -> {
                binding.iconIV.setImageResource(R.drawable.icon_toilet)
            }
            else-> Unit
        }

    }
}