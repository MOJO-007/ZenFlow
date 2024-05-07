package com.example.zenflow.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zenflow.R
import com.example.zenflow.adapters.MusicAdapter
import com.example.zenflow.databinding.FragmentGuidedBinding
import com.example.zenflow.databinding.FragmentLoginBinding
import com.example.zenflow.models.Music
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class GuidedFragment : Fragment() {

    private lateinit var binding: FragmentGuidedBinding
    private val audioUrls: ArrayList<String> = ArrayList()
    private lateinit var adapter: MusicAdapter
    private lateinit var dbref: CollectionReference
    private var musicList=ArrayList<Music>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGuidedBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbref = FirebaseFirestore.getInstance().collection("guided")
        binding.recyclerViewMusic.layoutManager = LinearLayoutManager(context)
        adapter = MusicAdapter(requireActivity())
        binding.recyclerViewMusic.adapter = adapter

        dbref.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){
                    val data = document.data
                    var music = Music(
                        musicName = data["name"].toString(),
                        musicSound = data["sound"].toString(),
                        thumbnailImage = data["image"].toString()
                    )
                    musicList.add(music)
//                    Log.d("goals", goals.toString())
//                    goals.sortBy { it.timestamp }
                    adapter.saveData(musicList)
                    binding.recyclerViewMusic.scrollToPosition(musicList.size - 1)
                }
            }.addOnFailureListener{
//                Log.e("Goals fetch", "Failure")
            }
    }

}