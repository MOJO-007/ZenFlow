package com.example.zenflow.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityRegLogBinding
import com.example.zenflow.databinding.FragmentLoginBinding
import com.example.zenflow.ui.activity.MainContainerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: CollectionReference
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentLoginBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbref = FirebaseFirestore.getInstance().collection("user")

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainContainerActivity::class.java)

//                    val q = dbref.whereEqualTo("userEmail",email)
//                    q.get().addOnCompleteListener {
//                        val ds = it.result?.getDocuments()?.get(0)
//                    }

                    intent.putExtra("userEmail",email)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    // Login failed
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}