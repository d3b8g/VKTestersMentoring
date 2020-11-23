package net.d3b8g.vktestersmentoring.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.helper.PathHelper.Companion.audioPath
import net.d3b8g.vktestersmentoring.modules.AudioAdapterModule
import java.io.File
import java.lang.Exception


class GalleryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var audioCounter:ArrayList<AudioAdapterModule> = ArrayList()

    fun update():Boolean{
        try {
            for ((index,file) in (File(audioPath).listFiles().withIndex())){
                audioCounter.add(AudioAdapterModule(
                    file_path = file.path,
                    file_title = "$index"
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
                Log.e("RRR","start ${inf.file_path}")
            }
        }
    }
}