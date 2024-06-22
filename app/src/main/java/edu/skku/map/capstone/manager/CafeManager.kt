package edu.skku.map.capstone.manager
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.util.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import edu.skku.map.capstone.models.cafe.KakaoCafe
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CafeManager private constructor() {

    companion object {
        private var instance: CafeManager? = null
        fun getInstance(): CafeManager {
            if (instance == null) {
                instance = CafeManager()
            }
            return instance!!
        }
    }
    var kakaoCafes = MutableLiveData<ArrayList<KakaoCafe>>(arrayListOf())
    var cafes = MutableLiveData<ArrayList<Cafe>>(arrayListOf())
    //history
    var lastSearchLat = DEFAULT_LAT
    var lastSearchLng = DEFAULT_LNG
    val searchText: MutableLiveData<String> = MutableLiveData("")
    var radius = 400
    val filterCategory = MutableLiveData<ArrayList<String>>(arrayListOf())

    //fetch kakao cafes from kakao server and post
    fun fetchCafes(lat: Double?, lng: Double?) {
        val lat = lat?: lastSearchLat
        val lng = lng?: lastSearchLng
        lastSearchLat = lat
        lastSearchLng = lng

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitService::class.java)

        Log.d("cafe", "fetching cafes from (${lat},${lng})")
        if(searchText.value!!.trim() == "") {
            service
                .fetchCafesKakao(
                    "KakaoAK 58cf96de0170b55a039a1779421eb7b0",
                    "CE7",
                    lng.toString(),
                    lat.toString(),
                    radius
                )
                .enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val kakaoCafeList = ArrayList<KakaoCafe>()
                        val body = response.body()!!
                        val jsonArray = JSONObject(body.string()).getJSONArray("documents")
                        Log.d("cafe", "cafeData ${jsonArray}")
                        for (i in 0 until jsonArray.length()) {
                            val kakaoJsonObject = jsonArray.getJSONObject(i)
//                            Log.d("cafe", kakaoJsonObject.toString())
                            val kakaoCafe = KakaoCafe(kakaoJsonObject)
                            kakaoCafeList.add(kakaoCafe)
                        }
                        Log.d("cafe", "total ${kakaoCafeList.size} cafe fetched from kakao" + kakaoCafeList)
                        kakaoCafes.postValue(kakaoCafeList)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                    }
                })
        }
        else{
            service
                .fetchCafesByKeywordKakao(
                    "KakaoAK 58cf96de0170b55a039a1779421eb7b0",
                    searchText.value!!,
                    "CE7",
                    lng.toString(),
                    lat.toString(),
                    radius
                )
                .enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val kakaoCafeList = ArrayList<KakaoCafe>()
                        val body = response.body()!!
                        val jsonArray = JSONObject(body.string()).getJSONArray("documents")
                        Log.d("cafe", "cafeData ${jsonArray}")
                        for (i in 0 until jsonArray.length()) {
                            val kakaoJsonObject = jsonArray.getJSONObject(i)
                            Log.d("cafe", kakaoJsonObject.toString())
                            val kakaoCafe = KakaoCafe(kakaoJsonObject)
                            kakaoCafeList.add(kakaoCafe)
                        }
                        Log.d(
                            "cafe",
                            "total ${kakaoCafeList.size} cafe fetched:" + kakaoCafeList.toString()
                        )
                        kakaoCafes.postValue(kakaoCafeList)
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("cafe", "failed to fetch cafes: ${t.localizedMessage}")
                    }
                })
        }
    }



    fun getFavoriteCafeList():ArrayList<Cafe> {
        return arrayListOf()
    }
}
