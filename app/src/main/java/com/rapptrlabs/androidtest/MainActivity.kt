package com.rapptrlabs.androidtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rapptrlabs.androidtest.animation.AnimationActivity
import com.rapptrlabs.androidtest.chat.ChatActivity
import com.rapptrlabs.androidtest.databinding.ActivityMainBinding
import com.rapptrlabs.androidtest.login.LoginActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.activity_main_title)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    private fun clickEvents() {
        binding.chatLayout.setOnClickListener { navigateToNextScreen(this, ChatActivity::class.java) }
        binding.loginLayout.setOnClickListener { navigateToNextScreen(this, LoginActivity::class.java) }
        binding.animationLayout.setOnClickListener { navigateToNextScreen(this, AnimationActivity::class.java) }
    }
}