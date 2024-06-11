package edu.skku.map.capstone.view.mycafe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.user.User
//import edu.skku.map.capstone.models.favorite.Favorite

class MyCafeViewModel() {
    private val _favoriteCafeList: MutableLiveData<List<Cafe>> = MutableLiveData<List<Cafe>>() // 즐겨찾기한 카페
//    private val _recommendCafeList: MutableLiveData<List<Cafe>> =MutableLiveData<List<Cafe>>() // 추천 카페
//    private val _visitedCafeList:MutableLiveData<List<Cafe>> =MutableLiveData<List<Cafe>>() // 방문한 카페

    val favoriteCafeList:LiveData<List<Cafe>> get() = _favoriteCafeList
//    val recommendCafeList: LiveData<List<Cafe>> get() = _recommendCafeList
//    val visitedCafeList: LiveData<List<Cafe>> get() = _visitedCafeList
    init {
//        _favoriteCafeList.value = listOf()
//        _recommendCafeList.value = listOf()
//        _visitedCafeList.value = listOf()
    }
    fun updateFavoriteCafeList() {
        _favoriteCafeList.value = User.favorites
    }
    // 백엔드로부터 받으면, 없어도 되는 데이터
    private val DEFAULT_LAT = 37.402005
    private val DEFAULT_LNG = 127.108621
    private val DEFAULT_RADIUS = 500

//    fun fetchCafes(lat:Double?, lng: Double?) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://dapi.kakao.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val service = retrofit.create(RetrofitService::class.java)
//
//        Log.d(
//            "cafe",
//            "fetching cafes from (${lat?.toString() ?: DEFAULT_LNG.toString()},${lng?.toString() ?: DEFAULT_LAT.toString()})"
//        )
//
//        service
//            .getCafes(
//                "KakaoAK f1c681d34107bd5d150c0bc5bd616975",
//                "CE7",
//                lat?.toString() ?: DEFAULT_LNG.toString(),
//                lng?.toString() ?: DEFAULT_LAT.toString(),
//                DEFAULT_RADIUS
//            )
//            .enqueue(object : Callback<ResponseBody> {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    val newCafeList = mutableListOf<Cafe>()
//                    val body = response.body()!!
//                    val jsonObject = JSONObject(body.string())
//                    val cafeData = jsonObject.getJSONArray("documents")
//
//                    for (i in 0 until cafeData.length()) {
//                        val cafeJsonObject = cafeData.getJSONObject(i)
//                        val cafe = Cafe(cafeJsonObject)
//                        newCafeList.add(cafe)
//                    }
//                    _recommendCafeList.value = newCafeList
//                    _visitedCafeList.value = newCafeList
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
//                }
//            })
//    }
}