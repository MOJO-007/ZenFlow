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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.zenflow.R
import com.example.zenflow.databinding.FragmentRegisterBinding
import com.example.zenflow.databinding.FragmentUserDetailsBinding
import com.example.zenflow.ui.activity.EditProfileActivity
import com.example.zenflow.ui.activity.MainContainerActivity
import com.example.zenflow.ui.activity.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.storage


class UserDetailsFragment : Fragment() {

    private val rq = 1
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var dbref: CollectionReference

    private lateinit var binding: FragmentUserDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbref = FirebaseFirestore.getInstance().collection("user")
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userName = arguments?.getString("userName")
        val id: String? = arguments?.getString("id")
        var imageUri: Uri? = arguments?.getString("ImageUri")?.toUri()
        val email = arguments?.getString("userEmail")
        val userMobileNumber = arguments?.getString("userMobileNumber")
//        Toast.makeText(requireContext(), "$imageUri", Toast.LENGTH_LONG).show()
        if (imageUri == null) {
            Glide.with(this)
                .load(R.drawable.ic_add_image)
                .error((R.drawable.ic_add_image))
                .into(binding.userImage)
        } else {
            Glide.with(this)
                .load(imageUri)
                .error(R.drawable.ic_add_image)
                .into(binding.userImage)
            binding.textViewUserNAme.setText(userName)
        }




        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()


        val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val storage = Firebase.storage
            val storageRef = storage.reference.child("pictures/$id")
            imageUri = it
            val fileRef = storageRef.child("pictures")
            val uploadTask = fileRef.putFile(Uri.parse(imageUri.toString()))

            uploadTask.addOnCompleteListener { _ ->
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    binding.userImage.setImageURI(it)
                    val imUri = uri.toString()
                    val userDocRef = id?.let { it1 -> dbref.document(it1) }
                    userDocRef?.update("imageUri", imUri)
                        ?.addOnSuccessListener {
                            Log.e("Done", "Donee")
                        }
                }
            }
            binding.userImage.setImageURI(it)
        }
        binding.userImage.setOnClickListener {
            resultLauncher.launch("image/*")
        }

        binding.btnLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("userEmail", email)
            intent.putExtra("userName", userName)
            intent.putExtra("imageUri", imageUri)
            intent.putExtra("userMobileNumber", userMobileNumber)

            startActivity(intent)
//            requireActivity().finish()
        }

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            val q = dbref.whereEqualTo("userEmail", email)
            q.get().addOnCompleteListener {
                val ds = it.result?.getDocuments()?.get(0)
                userName = ds?.getString("userName").toString()
                binding.textViewUserNAme.setText(userName)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
