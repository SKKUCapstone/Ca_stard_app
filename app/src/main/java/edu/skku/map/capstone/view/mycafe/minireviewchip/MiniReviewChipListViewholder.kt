package edu.skku.map.capstone.view.mycafe.minireviewchip

import android.content.Context
import android.opengl.Visibility
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ItemMiniCategorychipBinding

class MiniReviewChipListViewholder(val context: Context, var binding: ItemMiniCategorychipBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(reviewText: String){
        binding.scoreTV.visibility = View.GONE
        when(reviewText){
            "bright" -> {
                binding.iconIV.setImageResource(R.drawable.icon_bright_faded)
            }
            "clean" -> {
                binding.iconIV.setImageResource(R.drawable.icon_clean_faded)
            }
            "quiet" -> {
                binding.iconIV.setImageResource(R.drawable.icon_quiet_faded)
            }
            "capacity" -> {
                binding.iconIV.setImageResource(R.drawable.icon_capacity_faded)
            }
            "powerSocket" -> {
                binding.iconIV.setImageResource(R.drawable.icon_powersocket_faded)
            }
            "wifi" -> {
                binding.iconIV.setImageResource(R.drawable.icon_wifi_faded)
            }
            "tables" -> {
                binding.iconIV.setImageResource(R.drawable.icon_tables_faded)
            }
            "toilet" -> {
                binding.iconIV.setImageResource(R.drawable.icon_toilet_faded)
            }
            else-> Unit
        }
    }

}