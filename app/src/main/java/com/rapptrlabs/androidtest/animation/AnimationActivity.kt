package com.rapptrlabs.androidtest.animation



import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.rapptrlabs.androidtest.R
import com.rapptrlabs.androidtest.databinding.ActivityAnimationBinding


class AnimationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnimationBinding
    private var initialX = 0f
    private var initialY = 0f
    private var originalX = 0f
    private var originalY = 0f
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.activity_animation_title)
        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        clickEvents()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickEvents() {
        binding.fadeInBtn.setOnClickListener {
            val animator = ObjectAnimator.ofFloat(binding.dAndAImg, "alpha", 1f, 0f, 1f)
            animator.duration = 1000
            animator.start()
        }

        binding.dAndAImg.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.rawX
                    initialY = event.rawY
                    originalX = view.x
                    originalY = view.y
                    startSound()
                    burstFireworks()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - initialX
                    val dy = event.rawY - initialY
                    val newX = originalX + dx
                    val newY = originalY + dy
                    view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    view.animate()
                        .x(originalX)
                        .y(originalY)
                        .setInterpolator(DecelerateInterpolator())
                        .setDuration(500)
                        .withEndAction { stopSound() }
                        .start()
                }
            }
            true
        }
    }

    private fun startSound() {
        Toast.makeText(this, "Playing Blinding Lights by The Weekend", Toast.LENGTH_SHORT).show()
        mediaPlayer = MediaPlayer.create(this, R.raw.the_weeknd_binding_lights)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        Toast.makeText(this, "Continue dragging to play song", Toast.LENGTH_SHORT).show()
    }

    private fun stopSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    private fun burstFireworks() {
        val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(10, 30)
        val cracker = LottieAnimationView(this)
        cracker.layoutParams = lp
        cracker.setAnimation(R.raw.fireworks)
        cracker.layoutParams.height = Int.MAX_VALUE
        cracker.layoutParams.width = Int.MAX_VALUE
        cracker.x = 10.toFloat()
        cracker.y = 30.toFloat()
        binding.animationLayout.addView(cracker)
        cracker.playAnimation()
    }
}