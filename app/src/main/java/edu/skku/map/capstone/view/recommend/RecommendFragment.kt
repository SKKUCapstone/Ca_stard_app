package edu.skku.map.capstone.view.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import edu.skku.map.capstone.databinding.FragmentRecommendBinding
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.view.home.cafelist.CafeListAdapter
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendFragment : Fragment() {
    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!
    private val recommendCafeList = MutableLiveData<List<Cafe>>(listOf()) // 추천 카페
    // RecyclerView
    private lateinit var recommendCafeListAdapter: RecommendCafeListAdapter
    // Todo: make OncafeClick Logic
//    private val onCafeClick = MutableLiveData<Cafe>()
    //Todo: fetch current location data
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater, container, false)
        initUI()
        fetchRecommendation(DEFAULT_LAT, DEFAULT_LNG, User.id)
        observeRecommendList()
        return binding.root
    }

    private fun initUI() {
        recommendCafeListAdapter = RecommendCafeListAdapter(requireContext(), recommendCafeList.value!!)
        Log.d("InitUI", "recommendCafeList.value: ${recommendCafeList.value!!}")
        binding.recommendListRV.adapter = recommendCafeListAdapter
    }

    private fun fetchRecommendation(lat: Double?, lng: Double?, userId: Long) {
        val retrofit = Retrofit.Builder()
//            .baseUrl("https://dapi.kakao.com/")
            .baseUrl("http://43.201.119.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d(
            "cafe",
            "fetching recommend cafes from (${DEFAULT_LAT?.toString() ?: DEFAULT_LNG.toString()},${DEFAULT_LNG?.toString() ?: DEFAULT_LAT.toString()}, , UserID: ${User.id})"
        )

        service
            .getRecommendCafes(
                (lng ?: DEFAULT_LNG),
                (lat ?: DEFAULT_LAT),
                userId
            )
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val newCafeList = mutableListOf<Cafe>()
                    val body = response.body()

                    if (body != null) {
                        val jsonArray = JSONArray(body.string())
                        Log.d("cafe", "recommendCafeData ${jsonArray}")
                        for (i in 0 until jsonArray.length()) {
                            val cafeJsonObject = jsonArray.getJSONObject(i)
//                        Log.d("cafe", cafeJsonObject.toString())
                            val cafe = Cafe(cafeJsonObject)
                            newCafeList.add(cafe)
                        }
                        Log.d(
                            "cafe",
                            "total ${newCafeList.size} cafe fetched:" + newCafeList.toString()
                        )
                        recommendCafeList.postValue(newCafeList)
                    }
                    else{
                        Log.d("cafe", "Response body is null")
                        recommendCafeList.postValue(emptyList()) // 빈 리스트를 포스트하여 NullPointerException 방지
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                    recommendCafeList.postValue(emptyList()) // 실패 시 빈 리스트를 포스트하여 NullPointerException 방지
                }
            })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecommendList() {
        recommendCafeList.observe(viewLifecycleOwner) { newCafeList ->
            Log.d("observeRecommendList", "newCafeList: $newCafeList")
            recommendCafeListAdapter.updateCafes(newCafeList)
        }
    }

}