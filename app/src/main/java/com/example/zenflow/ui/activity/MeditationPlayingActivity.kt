package com.example.zenflow.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityMeditationPlayingBinding
import com.example.zenflow.databinding.ActivityWelcomeBinding
import com.example.zenflow.ui.fragment.UserDetailsFragment
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MeditationPlayingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeditationPlayingBinding

    val mediaPlayer = MediaPlayer()
    private lateinit var playPauseButton: ImageButton
    private lateinit var musicSeekBar: SeekBar
    private var updateThread: Thread? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeditationPlayingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicName = intent.getStringExtra("name")
        val thumbnailImage = intent.getStringExtra("thumbnail")
//        Toast.makeText(this, "$thumbnailImage", Toast.LENGTH_LONG).show()
        val storageRef = FirebaseStorage.getInstance().reference
        val musicRef = storageRef.child("music/$musicName.mp3")
//        Toast.makeText(this, "$musicRef", Toast.LENGTH_SHORT).show()
        val localFile = File(this.getExternalFilesDir(null), "$musicName.mp3")
        playPauseButton = binding.btnPlayPause
        musicSeekBar = binding.seekBar

        Glide.with(this)
            .load(thumbnailImage)
            .error(R.drawable.ic_add_image)
            .into(binding.imageThumbnail)
//            .into(binding.imgThumbnail)
        if (localFile.exists()) {
//            Toast.makeText(this, "DID NOT DOWNLOAD", Toast.LENGTH_SHORT).show()
            playMusic(localFile)
        } else {
            // Music file doesn't exist locally, download it
//            Toast.makeText(this, "DOWNLOADED", Toast.LENGTH_SHORT).show()
            musicRef.getFile(localFile)
                .addOnSuccessListener {
                    // File downloaded successfully, play it
                    playMusic(localFile)
                }
                .addOnFailureListener {
                    // Handle unsuccessful downloads
                }
        }

        playPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            } else {
                mediaPlayer.start()
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            }
        }
        // Show a toast when the SeekBar is touched
//        musicSeekBar.setOnTouchListener { _, event ->
//            // Show a toast when the SeekBar is touched
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                Toast.makeText(this, "YOU CANNOT FAST FORWARD A MEDITATION", Toast.LENGTH_SHORT).show()
//            }
//            // Consume touch events to prevent seeking
//            true
//        }


        mediaPlayer.setOnPreparedListener {
            musicSeekBar.max = mediaPlayer.duration
            mediaPlayer.start()
        }

        musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed
            }
        })

        updateThread = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    Thread.sleep(1000)
                    runOnUiThread {
                        musicSeekBar.progress = mediaPlayer.currentPosition
//                        Toast.makeText(this, "${musicSeekBar.progress}, ${musicSeekBar.max}", Toast.LENGTH_SHORT).show()
                    }
                    if (musicSeekBar.progress >= musicSeekBar.max) {
                        // Perform other actions like starting a new activity
                        val intent = Intent(this@MeditationPlayingActivity, MeditationCompleteActivity::class.java)
                        startActivity(intent)
                        this@MeditationPlayingActivity.finish() // Assuming this code is inside an Activity class
                        // Interrupt the thread to stop it from running continuously
                        updateThread?.interrupt()
                    }
                }
            } catch (e: InterruptedException) {
                // Restore interrupt status
                Thread.currentThread().interrupt()
                e.printStackTrace()
            }
        }
        updateThread?.start()

        binding.imgBack.setOnClickListener{
            updateThread?.interrupt()
            finish() // Finish the current activity
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Stop the background thread
        updateThread?.interrupt()

        // Finish the current activity
        finish()
    }

    private fun playMusic(file: File) {
        // Play the music using MediaPlayer or ExoPlayer

        mediaPlayer.setDataSource(file.path)

        mediaPlayer.prepare()

        mediaPlayer.start()
    }
}