package com.example.zenflow.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zenflow.R
import com.example.zenflow.adapters.MusicAdapter
import com.example.zenflow.databinding.ActivityMeditationPlayingBinding
import com.example.zenflow.databinding.FragmentUnguidedBinding
import com.example.zenflow.models.Music
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class UnguidedFragment : Fragment() {
    private lateinit var binding: FragmentUnguidedBinding
    private lateinit var adapter: MusicAdapter
    private lateinit var dbref: CollectionReference
    private var musicList=ArrayList<Music>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentUnguidedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbref = FirebaseFirestore.getInstance().collection("music")
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