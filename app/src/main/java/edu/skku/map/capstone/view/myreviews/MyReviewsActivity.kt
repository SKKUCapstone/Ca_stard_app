package edu.skku.map.capstone.view.myreviews

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import edu.skku.map.capstone.databinding.ActivityMyReviewsBinding
import edu.skku.map.capstone.manager.MyReviewManager
import edu.skku.map.capstone.models.user.User

class MyReviewsActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyReviewsBinding
    lateinit var myReviewListAdapter: MyReviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        setClickListener()
        observeData()
    }

    private fun setUI() {
        supportActionBar?.hide()

        myReviewListAdapter = MyReviewListAdapter(this)
        binding.reviewRV.adapter = myReviewListAdapter
    }

    private fun setClickListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        MyReviewManager
            .getInstance()
            .reviews
            .observe(this) {
                myReviewListAdapter.reloadData(it)
            }
    }
}