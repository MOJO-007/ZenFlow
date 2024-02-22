package com.example.zenflow.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityMainContainerBinding
import com.example.zenflow.databinding.ActivityRegLogBinding
import com.example.zenflow.databinding.FragmentLoginBinding
import com.example.zenflow.ui.fragment.LoginFragment
import com.example.zenflow.ui.fragment.RegisterFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class RegLogActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var binding: ActivityRegLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegLogBinding.inflate(layoutInflater)

        setContentView(binding.root)

        dbref = FirebaseDatabase.getInstance().getReference("user")

        var yourFragment:Fragment
        val signInText:String=getString(R.string.signin)
        val signUpText:String=getString(R.string.signup)

        yourFragment=LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.LogRegFrame, yourFragment)
            .commit()

        binding.textViewSignLogin.setOnClickListener{

            var screen:String= binding.textViewSignLogin.text.toString()
            if (screen == signInText){
                binding.textView2.text=getString(R.string.log_in)
                yourFragment=LoginFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.LogRegFrame, yourFragment)
                    .commit()
                binding.textViewSignLogin.text=signUpText
            }
            if (screen==signUpText){
                binding.textView2.text="REGISTER"
                yourFragment=RegisterFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.LogRegFrame, yourFragment)
                    .commit()
                binding.textViewSignLogin.text=signInText
            }
        }
    }
}