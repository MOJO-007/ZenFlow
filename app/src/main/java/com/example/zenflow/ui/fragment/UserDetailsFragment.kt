package com.example.zenflow.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.zenflow.R
import com.example.zenflow.databinding.FragmentRegisterBinding
import com.example.zenflow.databinding.FragmentUserDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class UserDetailsFragment : Fragment() {

    private var imageUri:Uri?=null
    private lateinit var auth: FirebaseAuth

    private lateinit var dbref: CollectionReference

    private lateinit var binding: FragmentUserDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbref = FirebaseFirestore.getInstance().collection("user")
        binding= FragmentUserDetailsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        binding.userImage.setOnClickListener{
            resultLauncher.launch("image/*")
        }

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.userImage.setImageURI(it)
        binding.userImage.scaleType=ImageView.ScaleType.CENTER_CROP

    }
}
