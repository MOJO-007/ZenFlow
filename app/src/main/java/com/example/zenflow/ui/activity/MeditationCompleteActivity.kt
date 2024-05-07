package com.example.zenflow.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityMeditationCompleteBinding
import com.example.zenflow.databinding.ActivityMeditationPlayingBinding

class MeditationCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeditationCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeditationCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            this.finish()
        }

        binding.textback.setOnClickListener{
            this.finish()
        }
    }
}