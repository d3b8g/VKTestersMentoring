package net.d3b8g.vktestersmentoring.adapters

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mMicro
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mPlayer
import net.d3b8g.vktestersmentoring.helper.PathHelper.Companion.audioPath
import net.d3b8g.vktestersmentoring.modules.AudioAdapterModule
import java.io.File


class GalleryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var audioCounter:ArrayList<AudioAdapterModule> = ArrayList()

    fun update():Boolean{
        try {
            File(audioPath).listFiles()?.forEachIndexed { index, file ->
                audioCounter.add(AudioAdapterModule(
                    file_path = file.path,
                    file_title = "${index+1}"
                ))
            }
        }catch (e:Exception){}

        return if(audioCounter.size!=0) {
            notifyDataSetChanged()
            true
        }
        else false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_audio, parent, false)
        return GalleryAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is GalleryAdapterViewHolder) holder.bind(audioCounter[position])
    }

    override fun getItemCount(): Int {
        return audioCounter.size
    }

    class GalleryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var box = itemView.findViewById<RelativeLayout>(R.id.audio_box)
        var aid = itemView.findViewById<TextView>(R.id.aid)

        fun bind(inf:AudioAdapterModule){
            aid.text = inf.file_title
            box.setOnClickListener {
                if(mMicro == null && inf.file_path.contains("VKTMaudio") && inf.file_path.contains("-") && inf.file_path.takeLast(4)==".3gp"){
                    mPlayer = MediaPlayer().apply{
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                        setDataSource(inf.file_path)
                        prepare()
                        start()
                    }
                }else{
                    Toast.makeText(itemView.context,"Возможно, вы пытаетесь воспроизвести аудио поверх другого или запустить не медиа-файл",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}