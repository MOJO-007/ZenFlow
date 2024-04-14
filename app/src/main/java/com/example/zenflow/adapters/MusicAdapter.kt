package com.example.zenflow.adapters
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zenflow.R
import com.example.zenflow.models.Music
import com.example.zenflow.ui.activity.MeditationPlayingActivity
import com.example.zenflow.ui.activity.WelcomeActivity

class MusicAdapter(private val context: Context) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.musicName == newItem.musicName
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            Log.d("data same", "${oldItem == newItem}")
            return oldItem == newItem
        }
    }


    private val differ = AsyncListDiffer(this,differCallBack)

    fun saveData(dataResponse: List<Music>) {
        differ.submitList(dataResponse.toMutableList())
        Log.d("data saved", "$dataResponse")
    }

    inner class MusicViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) {
        val textName : TextView = itemView.findViewById(R.id.textMusicName)
        val imgMusic : ImageView = itemView.findViewById(R.id.imgThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        Log.d("differ in adapter","here")
        val view = LayoutInflater.from(context).inflate(R.layout.music_holder, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val currentMusic = differ.currentList[position]
        holder.textName.text = currentMusic.musicName
        Log.d("Music Adapter","${currentMusic.musicName}")
        holder.itemView.setOnClickListener() {
            val intent = Intent(context, MeditationPlayingActivity::class.java)
            intent.putExtra("name",currentMusic.musicName)
            intent.putExtra("thumbnail",currentMusic.thumbnailImage)
            context.startActivity(intent)
        }

        Glide.with(holder.itemView.context)
            .load(currentMusic.thumbnailImage)
            .placeholder(R.drawable.img_user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)// Add a placeholder drawable
            .error(R.drawable.img_user)
            .into(holder.imgMusic)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}