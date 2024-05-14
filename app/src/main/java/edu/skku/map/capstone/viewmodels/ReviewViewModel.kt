package edu.skku.map.capstone.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.slider.Slider
import edu.skku.map.capstone.R
import edu.skku.map.capstone.models.Cafe

class ReviewViewModel(private val context: Context ,val cafe: Cafe) {

    //category phase
//    val categorySelect = MutableLiveData(arrayListOf("capacity","bright","clean","wifi","quiet","tables","powerSocket","toilet"))
    val categorySelect = MutableLiveData(arrayListOf("wifi","quiet","tables","powerSocket"))

    //rating phase
    var capacityRating = 3
    var brightRating = 3
    var cleanRating = 3
    var wifiRating = 3
    var quietRating = 3
    var tablesRating = 3
    var powerSocketRating = 3
    var toiletRating = 3
    var textReview = ""


    fun onSubmitReview() {

    }

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
            if (value <= 1.0) {
                slider.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            } else if (value <= 3.0) {
                slider.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow))
            } else {
                slider.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            }

        }

    }

}