package edu.skku.map.capstone.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.databinding.DialogReviewRatingBinding

class ReviewDialogRating(activity: MainActivity, context: Context, private val phase: MutableLiveData<Int>):Dialog(context) {
    private lateinit var binding: DialogReviewRatingBinding
    private val activity = activity
    private val viewModel = activity.reviewViewModel!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogReviewRatingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setModalOptions()
        setUI()
        setClickListener()
        setSliderListener()
    }

    private fun setModalOptions() {
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    private fun setUI() {
        val height = (activity.resources.displayMetrics.heightPixels * 0.90).toInt()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height)
        window?.setGravity(Gravity.BOTTOM)
        if(!viewModel.categorySelect.value!!.contains("capacity")) {
            binding.ratingCapacity.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("bright")) {
            binding.ratingBright.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("clean")) {
            binding.ratingClean.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("wifi")) {
            binding.ratingWifi.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("quiet")) {
            binding.ratingQuiet.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("tables")) {
            binding.ratingTables.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("toilet")) {
            binding.ratingToilet.visibility = View.GONE
        }
        if(!viewModel.categorySelect.value!!.contains("powerSocket")) {
            binding.ratingPowerSocket.visibility = View.GONE
        }
    }

    private fun setClickListener() {
        binding.nextBtn.setOnClickListener {
            phase.postValue((phase.value!!+1) % 4)
        }
        binding.cancelBtn.setOnClickListener {
            phase.postValue(0)
        }
    }

    private fun setSliderListener() {
        viewModel.initSliderListeners(
            binding.capacitySlider, binding.capacityTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.brightSlider, binding.brightTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.cleanSlider, binding.cleanTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.wifiSlider, binding.wifiTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.quietSlider, binding.quietTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.tablesSlider, binding.tablesTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.toiletSlider, binding.toiletTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )
        viewModel.initSliderListeners(
            binding.powerSocketSlider, binding.powerSocketTV, arrayOf(
                "",
                "공간이 협소해요",
                "살짝 좁아요",
                "아주 적당해요",
                "적당히 넓어요",
                "아주 넓어요",
            )
        )

    }

}