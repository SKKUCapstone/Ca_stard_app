package edu.skku.map.capstone.view.dialog.review.rating

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.databinding.DialogReviewRatingBinding
import edu.skku.map.capstone.view.dialog.review.ReviewViewModel

class ReviewDialogRating(private val viewModel: ReviewViewModel, context: Context, private val phase: MutableLiveData<Int>):Dialog(context) {
    private lateinit var binding: DialogReviewRatingBinding
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
        val height = (context.resources.displayMetrics.heightPixels * 0.65).toInt()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height)
        window?.setGravity(Gravity.BOTTOM)
        binding.cafeIdTV.text = viewModel.cafe.cafeName
        if(!viewModel.categorySelect.value!!.contains("capacity")) {
            binding.ratingCapacity.visibility = View.GONE
            viewModel.capacityRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("bright")) {
            binding.ratingBright.visibility = View.GONE
            viewModel.brightRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("clean")) {
            binding.ratingClean.visibility = View.GONE
            viewModel.cleanRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("wifi")) {
            binding.ratingWifi.visibility = View.GONE
            viewModel.wifiRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("quiet")) {
            binding.ratingQuiet.visibility = View.GONE
            viewModel.quietRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("tables")) {
            binding.ratingTables.visibility = View.GONE
            viewModel.tablesRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("toilet")) {
            binding.ratingToilet.visibility = View.GONE
            viewModel.toiletRating = 0
        }
        if(!viewModel.categorySelect.value!!.contains("powerSocket")) {
            binding.ratingPowerSocket.visibility = View.GONE
            viewModel.powerSocketRating = 0
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
        viewModel.setSliderListeners(
            "capacity",binding.capacitySlider, binding.capacityTV, arrayOf(
                "",
                "매우 좁아요",
                "다소 좁아요",
                "보통이애요",
                "넓어요",
                "아주 넓어요",
            )
        )
        viewModel.setSliderListeners(
            "bright", binding.brightSlider, binding.brightTV, arrayOf(
                "",
                "매우 어두워요",
                "다소 어두워요",
                "보통이애요",
                "밝아요",
                "아주 밝아요",
            )
        )
        viewModel.setSliderListeners(
            "clean", binding.cleanSlider, binding.cleanTV, arrayOf(
                "",
                "매우 불청결해요",
                "다소 불청결해요",
                "보통이애요",
                "청결해요",
                "아주 청결해요",
            )
        )
        viewModel.setSliderListeners(
            "wifi", binding.wifiSlider, binding.wifiTV, arrayOf(
                "",
                "매우 느려요",
                "다소 느려요",
                "보통이애요",
                "빨라요",
                "아주 빨라요",
            )
        )
        viewModel.setSliderListeners(
            "quiet", binding.quietSlider, binding.quietTV, arrayOf(
                "",
                "매우 시끄러워요",
                "다소 시끄러워요",
                "보통이애요",
                "조용해요",
                "아주 조용해요",
            )
        )
        viewModel.setSliderListeners(
            "tables", binding.tablesSlider, binding.tablesTV, arrayOf(
                "",
                "매우 좁아요",
                "다소 좁아요",
                "보통이애요",
                "넓어요",
                "아주 넓어요",
            )
        )
        viewModel.setSliderListeners(
            "toilet", binding.toiletSlider, binding.toiletTV, arrayOf(
                "",
                "매우 불청결해요",
                "다소 불청결해요",
                "보통이애요",
                "청결해요",
                "아주 청결해요",
            )
        )
        viewModel.setSliderListeners(
            "powerSocket", binding.powerSocketSlider, binding.powerSocketTV, arrayOf(
                "",
                "없어요",
                "매우 적어요",
                "다소 적어요",
                "보통이에요",
                "아주 적당해요",
            )
        )

    }

}