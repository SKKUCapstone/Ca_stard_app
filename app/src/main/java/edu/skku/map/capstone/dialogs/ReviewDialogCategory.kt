package edu.skku.map.capstone.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.DialogReviewCategoryBinding
import edu.skku.map.capstone.viewmodels.ReviewViewModel

class ReviewDialogCategory(private val activity: MainActivity,private val context: Context, private val phase: MutableLiveData<Int>):Dialog(context) {
    private lateinit var binding: DialogReviewCategoryBinding
    private val viewModel = activity.reviewViewModel!!

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
        binding.capacityBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("capacity")) prevList.remove("capacity")
            else prevList.add("capacity")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.brightBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("bright")) prevList.remove("bright")
            else prevList.add("bright")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.cleanBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("clean")) prevList.remove("clean")
            else prevList.add("clean")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.wifiBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("wifi")) prevList.remove("wifi")
            else prevList.add("wifi")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.quietBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("quiet")) prevList.remove("quiet")
            else prevList.add("quiet")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.tablesBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("tables")) prevList.remove("tables")
            else prevList.add("tables")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.powerSocketBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("powerSocket")) prevList.remove("powerSocket")
            else prevList.add("powerSocket")
            viewModel.categorySelect.postValue(prevList)
        }
        binding.toiletBtn.setOnClickListener {
            val prevList = viewModel.categorySelect.value!!
            if(prevList.contains("toilet")) prevList.remove("toilet")
            else prevList.add("toilet")
            viewModel.categorySelect.postValue(prevList)
        }
    }

    private fun observeCategorySelect() {
        val categoryList = listOf("capacity","bright","clean","wifi","quiet","tables","powerSocket","toilet")
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

        viewModel.categorySelect.observe(activity) { list->
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