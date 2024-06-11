package edu.skku.map.capstone.view.myreviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ActivityMyReviewsBinding
import edu.skku.map.capstone.models.user.User

class MyReviewsActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyReviewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        setClickListener()
    }

    private fun setUI() {
        supportActionBar?.hide()
        binding.reviewRV.adapter = MyReviewListAdapter(this, User.review)
    }

    private fun setClickListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}