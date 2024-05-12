package edu.skku.map.capstone.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.skku.map.capstone.databinding.ItemCafeListBinding
import edu.skku.map.capstone.models.Cafe
import edu.skku.map.capstone.viewholders.MycafeLikeListViewholder

class MyCafeLikeListAdapter(val context: Context) : RecyclerView.Adapter<MycafeLikeListViewholder>(){

    // 이미지로 바뀌어야 함
    private var cafeImagelist: List<Cafe> = listOf()


    // viewHolder를 만들떄 어떤 xml 사용할거?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MycafeLikeListViewholder {
        val binding = ItemCafeListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MycafeLikeListViewholder(binding)

    }

    // 데이터의 개수
    override fun getItemCount(): Int {
        return cafeImagelist.size
    }

    // 각 아이템에 대한 데이터를 설정하는 곳
    override fun onBindViewHolder(holder: MycafeLikeListViewholder, position: Int) {
        val cafeImage = cafeImagelist[position]
        holder.bind(cafeImage)
    }
}