package com.example.zenflow.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val userMobileNumber = intent.getStringExtra("userMobileNumber")

        binding.editTextUserName.setText(userName)
        binding.editTextMobileNumber.setText(userMobileNumber)
    }

}