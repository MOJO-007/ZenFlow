package com.example.zenflow.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zenflow.R
import com.example.zenflow.databinding.FragmentLoginBinding
import com.example.zenflow.databinding.FragmentRegisterBinding
import com.example.zenflow.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var dbref: CollectionReference

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dbref = FirebaseFirestore.getInstance().collection("user")
        binding= FragmentRegisterBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRegister.setOnClickListener {
            val altBuilder = AlertDialog.Builder(requireContext())
            val userName = binding.editTextUsername.text.toString()
            val userEmail = binding.editTextRegEmail.text.toString()
            val userPassword = binding.editTextRegPassword.text.toString()
            val userMobileNumber = binding.editTextPhone.text.toString()

            if(userPassword.length < 6){
                Toast.makeText(requireContext(), "Password must be of minimum 6 characters" ,Toast.LENGTH_SHORT).show()
            }

            else if(userName.isNotEmpty()&& userEmail.isNotEmpty()  && userPassword.isNotEmpty()){
                altBuilder.setTitle("Are you Sure?")
                    .setMessage("Pressing YES will register you username with the entered password")
                    .setPositiveButton("YES"){dialog,which->
                        registerUser(userName,userEmail,userPassword,userMobileNumber)
                        binding.editTextUsername.text = null
                        binding.editTextRegEmail.text=null
                        binding.editTextRegPassword.text=null
                        binding.editTextPhone.text=null
                    }
                    .setNegativeButton("NO"){dialog,which->
                        dialog.dismiss()
                    }
                    .show()
            }
            else{
                Toast.makeText(requireContext(), "Empty Fields are not allowed" ,Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun registerUser(username:String, email:String, password:String, number:String){
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                val userMobileNumber=binding.editTextPhone.text.toString()
                if (task.isSuccessful) {
                    val user=User(
                        userName = username,
                        userEmail = auth.currentUser?.email,
                        userPassword = password,
                        imageUri = null,
                        userMobileNumber=number)
                    dbref.add(user)
                    // Registration successful, proceed to next step
                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to the appropriate screen
                } else {
                    Toast.makeText(context, "Registration failed.", Toast.LENGTH_SHORT).show()
                    val exc = task.exception
                    if (exc is FirebaseAuthUserCollisionException) {
                        Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()
                        }

                }
            }


    }
}