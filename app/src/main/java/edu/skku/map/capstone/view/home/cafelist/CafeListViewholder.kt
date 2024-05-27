package edu.skku.map.capstone.view.home.cafelist

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.view.home.cafelist.reviewchip.ReviewChipListAdapter
import edu.skku.map.capstone.databinding.ItemCafePreviewBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.review.Review

class CafeListViewholder(val context: Context, var binding:ItemCafePreviewBinding):RecyclerView.ViewHolder(binding.root) {
    //데이터를 View에 직접 연결해서 구성해주는 컴포넌트

    fun bind(cafe: Cafe){
        val reviewChipListAdapter = ReviewChipListAdapter(context, filterTopReviews(cafe.reviews))
        binding.cafeNameTV.text = cafe.cafeName
        binding.distanceTV.text = "${cafe.distance?.toInt()}KM"
        binding.ratingTV.text = getAverage(cafe.reviews).toString()
        binding.reviewChipRV.adapter = reviewChipListAdapter

        setClickListener()
    }

    private fun getAverage(reviews: ArrayList<Review>): Double {
        var total = 0.0
        for(review in reviews){
            total += review.total
        }
        if(reviews.isNotEmpty()) total /= reviews.size
        return total
    }

    private fun filterTopReviews(reviews:ArrayList<Review>):ArrayList<String> {
        val filteredList = arrayListOf<String>()
        val size = reviews.size
        var sum = 0
        for(review in reviews){ sum += review.bright }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("bright")
        sum = 0
        for(review in reviews){ sum += review.clean }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("clean")
        sum = 0
        for(review in reviews){ sum += review.quiet }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("quiet")
        sum = 0
        for(review in reviews){ sum += review.capacity }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("capacity")
        sum = 0
        for(review in reviews){ sum += review.powerSocket }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("powerSocket")
        sum = 0
        for(review in reviews){ sum += review.wifi }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("wifi")
        sum = 0
        for(review in reviews){ sum += review.tables }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("table")
        sum = 0
        for(review in reviews){ sum += review.toilet }
        if(sum.toDouble()/size.toDouble() > 3.5 ) filteredList.add("toilet")

        return filteredList
    }

    private fun setClickListener() {
        binding.previewBodyCL.setOnClickListener{

        }
    }
}