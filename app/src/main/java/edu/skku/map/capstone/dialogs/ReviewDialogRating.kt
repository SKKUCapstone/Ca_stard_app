package edu.skku.map.capstone.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.databinding.DialogReviewRatingBinding

class ReviewDialogRating(activity: MainActivity, context: Context, private val phase: MutableLiveData<Int>):Dialog(context) {
    private lateinit var binding: DialogReviewRatingBinding
    private val activity = activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogReviewRatingBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)
        setModalOptions()
        setUI()
        setClickListener()
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
    }

    private fun setClickListener() {
        binding.nextBtn.setOnClickListener {
            phase.postValue((phase.value!!+1) % 4)
        }
        binding.cancelButton.setOnClickListener {
            phase.postValue(0)
        }
    }
}