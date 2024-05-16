package edu.skku.map.capstone.view.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import edu.skku.map.capstone.MainActivity
import edu.skku.map.capstone.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_TIME_OUT:Long = 3800;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // animation
        val fadeOutViews = listOf(binding.splashTextFadeout1, binding.splashTextFadeout2,binding.splashTextFadeout3)
        val fadeInViews = listOf(binding.splashTextFadein1)

        applyFadeAnimation(fadeOutViews, fadeInViews)

        Handler().postDelayed({
            // This method will be executed once the time is over
            // Start your app main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // close this Activity
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun applyFadeAnimation(fadeOutViews: List<View>, fadeInViews: List<View>) {
        val fadeOutAnimation  = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 2000
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    fadeOutViews.forEach { textView ->
                        textView.visibility = View.INVISIBLE
                    }
                    moveAllTexts()
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        fadeOutViews.forEach { textView ->
            textView.startAnimation(fadeOutAnimation)
        }
        fadeInAnimation(fadeInViews)
    }

    private fun fadeInAnimation(fadeInViews: List<View>) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 1000
            fillAfter = true
        }
        fadeInViews.forEach { textView ->
            textView.visibility = View.VISIBLE
            textView.startAnimation(fadeIn)
        }
    }

    private fun moveAllTexts() {
        // 모든 TextView를 동시에 이동하는 애니메이션 설정
        animateTextView(binding.splashTextRemain1, -120f, 200f)
        animateTextView(binding.splashTextRemain2, 175f, -200f)
        animateTextView(binding.splashTextFadein1, 0f, -200f)
        animateTextView(binding.splashTextRemain3, 0f, -200f)
    }

    private fun animateTextView(textView: TextView, translationX: Float, translationY: Float) {
        // 애니메이션 생성
        val animatorX = ObjectAnimator.ofFloat(textView, "translationX", 0f, translationX)
        val animatorY = ObjectAnimator.ofFloat(textView, "translationY", 0f, translationY)

        // 애니메이션 속도 및 지속 시간 설정
        animatorX.duration = 1500
        animatorY.duration = 1500

        // 애니메이션 시작
        animatorX.start()
        animatorY.start()
    }
}