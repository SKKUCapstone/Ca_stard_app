package edu.skku.map.capstone.view.mycafe.minireviewchip

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ItemMiniCategorychipBinding

class MiniReviewChipListViewholder(val context: Context, var binding: ItemMiniCategorychipBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(reviewText: String){
            when(reviewText){
                "bright" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_bright_resize)
                }
                "clean" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_clean_resize)
                }
                "quiet" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_quiet_resize)
                }
                "capacity" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_capacity_resize)
                }
                "powerSocket" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_powersocket_resize)
                }
                "wifi" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_wifi_resize)
                }
                "tables" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_table_resize)
                }
                "toilet" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_toilet_resize)
                }
                else-> Unit
            }
    }

}