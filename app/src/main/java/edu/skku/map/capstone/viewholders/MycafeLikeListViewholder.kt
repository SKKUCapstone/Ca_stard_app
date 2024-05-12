package edu.skku.map.capstone.viewholders
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ItemCafeListBinding
import edu.skku.map.capstone.models.Cafe

class MycafeLikeListViewholder(private val binding: ItemCafeListBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(cafe: Cafe) {
        binding.cafeImage.setImageResource(R.drawable.defaultcafepin)
    }
}