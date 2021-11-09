package net.d3b8g.vktestersmentoring.ui.gallery

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.helper.Components.mPlayer
import net.d3b8g.vktestersmentoring.ui.dictaphone.DictaphoneFragment.Companion.mMicro
import net.d3b8g.vktestersmentoring.ui.gallery.Gallery.getAudioModelList
import net.d3b8g.vktestersmentoring.ui.gallery.Gallery.getGallerySize


class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var audioCounter: ArrayList<AudioAdapterModel> = ArrayList()

    fun updateGalleryAdapter(context: Context): Boolean = if (context.getGallerySize() > 0) {
        audioCounter.clear()
        audioCounter.addAll(context.getAudioModelList())
        notifyDataSetChanged()
        true
    } else false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_audio, parent, false)
        return GalleryAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GalleryAdapterViewHolder) holder.bind(audioCounter[position])
    }

    override fun getItemCount(): Int = audioCounter.size

    class GalleryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val audioBox: RelativeLayout = itemView.findViewById(R.id.audio_box)
        private val audioId: TextView = itemView.findViewById(R.id.aid)

        fun bind(inf: AudioAdapterModel) {
            audioId.text = inf.file_title
            audioBox.setOnClickListener {
                if (mMicro == null && inf.file_path.contains("VKTMaudio")) {
                    mPlayer = MediaPlayer().apply {
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                        setDataSource(inf.file_path)
                        prepare()
                        start()
                    }
                } else {
                    Snackbar.make(itemView,
                        "Возможно, вы пытаетесь воспроизвести аудио поверх другого или запустить не медиа-файл",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}