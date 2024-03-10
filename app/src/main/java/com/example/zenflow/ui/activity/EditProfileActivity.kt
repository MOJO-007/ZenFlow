package com.example.zenflow.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: CollectionReference
    override fun onCreate(savedInstanceState: Bundle?) {

        dbref=FirebaseFirestore.getInstance().collection("user")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val id = intent.getStringExtra("id").toString()
        val userMobileNumber = intent.getStringExtra("userMobileNumber")

        binding.editTextUserName.setText(userName)
        binding.editTextMobileNumber.setText(userMobileNumber)

        binding.btnSaveChanges.setOnClickListener {
            val updates = hashMapOf<String, Any>()
            updates["userName"] = binding.editTextUserName.text.toString()
            updates["userMobileNumber"] = binding.editTextMobileNumber.text.toString()
            val docRef = dbref.document(id)
            docRef.update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this , "UserProfile Updated", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this , "Update Failed", Toast.LENGTH_LONG).show()
                }
            this.finish()
        }
    }


}