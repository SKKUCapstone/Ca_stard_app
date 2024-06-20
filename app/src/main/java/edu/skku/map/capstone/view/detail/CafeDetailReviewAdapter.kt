package edu.skku.map.capstone.view.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemReviewDetailBinding
import edu.skku.map.capstone.models.review.Review
import android.util.Log
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.FavoriteDTO
import edu.skku.map.capstone.util.RetrofitService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CafeDetailReviewAdapter(val context: Context): RecyclerView.Adapter<CafeDetailReviewViewHolder>() {
    private var reviewList: ArrayList<Review> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCafeList(newList: ArrayList<Review>) {
        Log.d("@@@review", newList.toString())
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
        val review = reviewList[position]
        holder.bind(review)
    }




}