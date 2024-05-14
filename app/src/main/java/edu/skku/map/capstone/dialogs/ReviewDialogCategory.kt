package edu.skku.map.capstone.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.DialogReviewCategoryBinding
import edu.skku.map.capstone.viewmodels.ReviewViewModel

class ReviewDialogCategory(private val viewModel: ReviewViewModel,private val context: Context, private val phase: MutableLiveData<Int>):Dialog(context) {
    private lateinit var binding: DialogReviewCategoryBinding
    private val categoryList = arrayListOf("capacity","bright","clean","wifi","quiet","tables","powerSocket","toilet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogReviewCategoryBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setModalOptions()
        setUI()
        setClickListener()
        observeCategorySelect()
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
        val btnList = arrayListOf(
            binding.capacityBtn,
            binding.brightBtn,
            binding.cleanBtn,
            binding.wifiBtn,
            binding.quietBtn,
            binding.tablesBtn,
            binding.powerSocketBtn,
            binding.toiletBtn,
        )

        binding.nextBtn.setOnClickListener {
            phase.postValue((phase.value!!+1) % 4)
        }
        binding.cancelButton.setOnClickListener {
            phase.postValue(0)
        }

        btnList.indices.map { idx->
            btnList[idx].setOnClickListener {
                toggleCategory(categoryList[idx])
            }
        }

        binding.selectAllRB.setOnClickListener {
            val fullList = arrayListOf<String>()
            fullList.addAll(categoryList)
            if((it as RadioButton).isChecked) {
                viewModel.categorySelect.postValue(fullList)
                binding.selectAllRB.isEnabled = false
            }
        }
    }

    private fun toggleCategory(category: String) {
        val prevList = viewModel.categorySelect.value!!
        if(prevList.contains(category)) {
            prevList.remove(category)
            binding.selectAllRB.isChecked = false
            binding.selectAllRB.isEnabled = true
        }
        else {
            prevList.add(category)
            if(prevList.size == categoryList.size) binding.selectAllRB.isChecked = true
        }
        viewModel.categorySelect.postValue(prevList)
    }

    private fun observeCategorySelect() {
        val layoutList = listOf(
            binding.capacityBtn,
            binding.brightBtn,
            binding.cleanBtn,
            binding.wifiBtn,
            binding.quietBtn,
            binding.tablesBtn,
            binding.powerSocketBtn,
            binding.toiletBtn
        )
        val iconList = listOf(
            binding.capacityIV,
            binding.brightIV,
            binding.cleanIV,
            binding.wifiIV,
            binding.quietIV,
            binding.tablesIV,
            binding.powerSocketIV,
            binding.toiletIV
        )

        viewModel.categorySelect.observe(context as LifecycleOwner) { list->
            categoryList.indices.forEach { idx ->
                if (list.contains(categoryList[idx])) {
                    layoutList[idx].background = ContextCompat.getDrawable(context, R.drawable.categorychip)
                    iconList[idx].alpha = 1.0F
                }
                else{
                    layoutList[idx].background = ContextCompat.getDrawable(context, R.drawable.categorychip_faded)
                    iconList[idx].alpha = 0.3F
                }
            }
        }
    }
}