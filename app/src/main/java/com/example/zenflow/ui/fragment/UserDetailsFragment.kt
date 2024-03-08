package com.example.zenflow.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.zenflow.R
import com.example.zenflow.databinding.FragmentRegisterBinding
import com.example.zenflow.databinding.FragmentUserDetailsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.storage


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
        var userName:String?
        var id:String=""
        var imageUri:Uri?
        val email = arguments?.getString("userEmail")
        val q = dbref.whereEqualTo("userEmail",email)
        q.get().addOnCompleteListener {
            val ds = it.result?.getDocuments()?.get(0)
            id=ds?.id.toString()
            userName= ds?.getString("userName").toString()
            imageUri= ds?.getString("imageUri")?.toUri()
            if (imageUri == null){
                Glide.with(this)
                    .load(R.drawable.ic_add_image)
                    .error((R.drawable.ic_add_image))
                    .into(binding.userImage)
            }
            Glide.with(this)
                .load(imageUri)
                .error(R.drawable.ic_add_image)
                .into(binding.userImage)
            binding.textViewUserNAme.setText(userName)
        }


        super.onViewCreated(view, savedInstanceState)
        auth=FirebaseAuth.getInstance()



         val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
             val storage = Firebase.storage
             val storageRef = storage.reference.child("pictures/$id")
             imageUri = it
             val fileRef = storageRef.child("pictures")
             val uploadTask = fileRef.putFile(Uri.parse(imageUri.toString()))

             uploadTask.addOnCompleteListener { _ ->
                 fileRef.downloadUrl.addOnSuccessListener { uri ->
                     binding.userImage.setImageURI(it)
                     val imUri = uri.toString()
                     val userDocRef = dbref.document(id)
                     userDocRef.update("imageUri", imUri)
                         .addOnSuccessListener {
                             Log.e("Done", "Donee")
                         }
                 }
             }
            binding.userImage.setImageURI(it)
        }
        binding.userImage.setOnClickListener{
            resultLauncher.launch("image/*")
        }
    }

}
