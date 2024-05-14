package edu.skku.map.capstone.viewmodels

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.google.android.material.slider.Slider

class ReviewViewModel {

    //category phase
    val categorySelect = MutableLiveData(arrayListOf("capacity","bright","clean","wifi","quiet","tables","powerSocket","toilet"))

    //rating phase
    var capacityRating = 3
    var brightRating = 3
    var cleanRating = 3
    var wifiRating = 3
    var quietRating = 3
    var tablesRating = 3
    var powerSocketRating = 3
    var toiletRating = 3
    var textInput = ""

    @SuppressLint("ResourceAsColor")
    fun initSliderListeners(slider: Slider, textView: TextView, textState: Array<String>) {

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
//            Log.d("slider", slider.trackActiveTintList.toString())
//            if(value <= 3.0){
//                slider.trackActiveTintList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(R.drawable.trackcolor_yellow))
//            } else {
//                slider.trackActiveTintList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(R.drawable.trackcolor_green))
//            }

        }

    }

}