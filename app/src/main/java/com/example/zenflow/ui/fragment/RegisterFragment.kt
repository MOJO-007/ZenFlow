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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

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
            val user=User(userName = binding.editTextUsername.text.toString(),
                userEmail = binding.editTextRegEmail.text.toString(),
                userPassword = binding.editTextRegPassword.text.toString())


            if(user.userName.isNotEmpty()&& user.userEmail.isNotEmpty()  && user.userPassword.isNotEmpty()){
                altBuilder.setTitle("Are you Sure?")
                    .setMessage("Pressing YES will register you username with the entered password")
                    .setPositiveButton("YES"){dialog,which->
                        registerUser(user)
                        binding.editTextUsername.text = null
                        binding.editTextRegEmail.text=null
                        binding.editTextRegPassword.text=null
                        Toast.makeText(requireContext(), "User Registered" ,Toast.LENGTH_SHORT).show()
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
    private fun registerUser(user:User){
        dbref.add(user).addOnCompleteListener{
            Log.d("Successfull Register","Registration done")
        }.addOnFailureListener{
            Log.d("Failure","Registration failed")
        }
    }
}