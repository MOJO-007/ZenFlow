package com.example.zenflow.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.zenflow.databinding.ActivityMainContainerBinding
import com.example.zenflow.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainContainerActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnGetStarted.setOnClickListener {
//            Toast.makeText(this , "$currentUser", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegLogActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}