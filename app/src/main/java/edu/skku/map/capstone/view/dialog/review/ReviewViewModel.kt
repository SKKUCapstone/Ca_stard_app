package edu.skku.map.capstone.view.dialog.review

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.slider.Slider
import edu.skku.map.capstone.R
import edu.skku.map.capstone.models.cafe.Cafe
import edu.skku.map.capstone.models.user.User
import edu.skku.map.capstone.util.RetrofitService
import edu.skku.map.capstone.util.ReviewDTO
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReviewViewModel(private val context: Context ,val cafe: Cafe) {

    //category phase
    val categorySelect = MutableLiveData(arrayListOf("wifi","quiet","tables","powerSocket"))

    //rating phase
    var capacityRating = 0
    var brightRating = 0
    var cleanRating = 0
    var wifiRating = 0
    var quietRating = 0
    var tablesRating = 0
    var powerSocketRating = 0
    var toiletRating = 0
    var textReview = ""



    @SuppressLint("ResourceAsColor")
    fun initSliderListeners(id: String, slider: Slider, textView: TextView, textState: Array<String>) {

        slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        slider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            textView.text = textState[value.toInt()]
            when(id) {
                "capacity" -> capacityRating = value.toInt()
                "bright" -> brightRating = value.toInt()
                "clean" -> cleanRating = value.toInt()
                "wifi" -> wifiRating = value.toInt()
                "quiet" -> quietRating = value.toInt()
                "tables" -> tablesRating = value.toInt()
                "toilet" -> toiletRating = value.toInt()
                "powerSocket" -> powerSocketRating = value.toInt()
            }

            Log.d("slider", slider.trackActiveTintList.toString())
            if (value <= 2.0) {
                slider.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            } else if (value <= 3.0) {
                slider.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow))
            } else {
                slider.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            }

        }

    }

}