package edu.skku.map.capstone.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.RetrofitService
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
    private val _liveCafeList: MutableLiveData<List<Cafe>> = MutableLiveData<List<Cafe>>() //viewModel 안에서만 수정
    val liveCafeList: LiveData<List<Cafe>> get() = _liveCafeList //뷰모델 밖에서 수정
    lateinit var cafeListFragment: CafeListFragment
    lateinit var cafeDetailFragment: CafeDetailFragment
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    private val DEFAULT_RADIUS = 500

    init {
        _liveCafeList.value = listOf()
    }
    fun refreshCafeList() {
        Log.d("cafe", "refreshed!")
    }
    fun fetchCafes(lat:Double?, lng: Double?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d("cafe","fetching cafes from (${lat?.toString()?:DEFAULT_LNG.toString()},${lng?.toString()?:DEFAULT_LAT.toString()})")

        service
            .getCafes(
                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
                "CE7",
                lat?.toString()?:DEFAULT_LNG.toString(),
                lng?.toString()?:DEFAULT_LAT.toString(),
                DEFAULT_RADIUS
            )
            .enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val newCafeList = mutableListOf<Cafe>()
                    val body = response.body()!!
                    val jsonObject = JSONObject(body.string())
                    val cafeData = jsonObject.getJSONArray("documents")

                    for (i in 0 until cafeData.length()) {
                        val cafeJsonObject = cafeData.getJSONObject(i)
                        val cafe = Cafe(cafeJsonObject)
                        newCafeList.add(cafe)
                    }
                    _liveCafeList.value = newCafeList

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                }
            })
    }




}