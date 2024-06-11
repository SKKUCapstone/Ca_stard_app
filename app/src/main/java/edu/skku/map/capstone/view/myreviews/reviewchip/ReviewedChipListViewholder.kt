package edu.skku.map.capstone.view.myreviews.reviewchip

import android.content.Context
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ItemMiniCategorychipBinding

class ReviewedChipListViewholder(val context:Context, var binding:ItemMiniCategorychipBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(review: Pair<String, Int>){
            when(review.first){
                "bright" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_bright_faded) }
                "clean" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_clean_faded)}
                "quiet" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_quiet_faded)}
                "capacity" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_capacity_faded)}
                "powerSocket" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_powersocket_faded)}
                "wifi" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_wifi_faded)}
                "tables" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_tables_faded)}
                "toilet" -> {
                    binding.iconIV.setImageResource(R.drawable.icon_toilet_faded)}
                else-> Unit
            }
        binding.scoreTV.text = review.second.toString()
        val mLayoutParams = binding.scoreTV.layoutParams as MarginLayoutParams
        mLayoutParams.marginStart = 8
        binding.scoreTV.layoutParams = mLayoutParams
        binding.reviewChipLL.setPadding(30,10,30,10)

    }
}