package edu.skku.map.capstone.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.FragmentManager
import edu.skku.map.capstone.R
import edu.skku.map.capstone.RetrofitService
import edu.skku.map.capstone.adapters.CafePreviewListAdapter
import edu.skku.map.capstone.fragments.CafeDetailFragment
import edu.skku.map.capstone.fragments.CafeListFragment
import edu.skku.map.capstone.models.Cafe
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel() {
    val cafeList: ArrayList<Cafe> = arrayListOf()
    lateinit var cafeListAdapter: CafePreviewListAdapter
//    lateinit var childFragmentManager: FragmentManager
    lateinit var cafeListFragment: CafeListFragment
    lateinit var cafeDetailFragment: CafeDetailFragment
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    private val DEFAULT_RADIUS = 500
    @SuppressLint("NotifyDataSetChanged")
    fun refreshCafeList(newCafeList: ArrayList<Cafe>) {
        cafeList.clear()
        cafeList.addAll(newCafeList)
        cafeListAdapter.notifyDataSetChanged()

    }

    fun fetchCafes(lat:Double?, lng: Double?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d("cafe","fetching cafes from (${lat?.toString()?:DEFAULT_LNG.toString()},${lng?.toString()?:DEFAULT_LAT.toString()}")

        service
            .getCafes(
                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
                "CE7",
                lat?.toString()?:DEFAULT_LNG.toString(),
                lng?.toString()?:DEFAULT_LAT.toString(),
                DEFAULT_RADIUS
            )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val cafeList = arrayListOf<Cafe>()
                    val body = response.body()!!
                    val jsonObject = JSONObject(body.string())
                    val cafeData = jsonObject.getJSONArray("documents")

                    for (i in 0 until cafeData.length()) {
                        val cafeJsonObject = cafeData.getJSONObject(i)
                        val cafe = Cafe(cafeJsonObject)
                        cafeList.add(cafe)
                    }
                    Log.d("cafe", cafeList.toString())
                    refreshCafeList(cafeList)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                }
            })
    }




}