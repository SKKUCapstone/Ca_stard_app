package edu.skku.map.capstone.view.dialog.review.comment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.DialogReviewCommentBinding
import edu.skku.map.capstone.view.dialog.review.ReviewViewModel

class ReviewDialogComment(private val viewModel: ReviewViewModel, context: Context, private val phase: MutableLiveData<Int>):Dialog(context) {
    private lateinit var binding: DialogReviewCommentBinding
    private val MAX_TEXTLEN = 400
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogReviewCommentBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setModalOptions()
        setUI()
        setClickListener()
        listenEditText()
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
    }

    private fun setClickListener() {
        binding.nextBtn.setOnClickListener {
            viewModel.onSubmitReview()
            phase.postValue((phase.value!!+1) % 4)
        }
        binding.cancelButton.setOnClickListener {
            phase.postValue(0)
        }
    }

    private fun listenEditText() {
        binding.commentET.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textLenTV.text = "${s!!.length}/$MAX_TEXTLEN"
                viewModel.textReview = binding.commentET.text.toString().trim()
                if(s.length >= MAX_TEXTLEN) {
                    binding.textLenTV.setTextColor(context.getColor(R.color.red))
                }
                else binding.textLenTV.setTextColor(context.getColor(R.color.black))
            }
            override fun afterTextChanged(s: Editable?) { }
        })
    }
}