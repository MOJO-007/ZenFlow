package com.example.zenflow.ui.fragment

import android.os.Bundle
import android.util.Log
import android.util.Log.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            val user=User(binding.editTextUsername.text.toString(),
                binding.editTextRegEmail.text.toString(),
                binding.editTextRegPassword.text.toString())
            registerUser(user)
            binding.editTextUsername.text = null
            binding.editTextRegEmail.text=null
            binding.editTextRegPassword.text=null
        }
    }
    private fun registerUser(user:User){
        dbref.add(user).addOnCompleteListener{
            Log.d("Successfull Register","Register done baby")
        }.addOnFailureListener{
            Log.d("Failure","Registration nhi hua")
        }
    }
}