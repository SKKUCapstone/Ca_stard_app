package edu.skku.map.capstone.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.skku.map.capstone.R
import edu.skku.map.capstone.databinding.ActivityReviewBinding

class ReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReviewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}