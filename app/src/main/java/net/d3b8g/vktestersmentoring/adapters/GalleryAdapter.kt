package net.d3b8g.vktestersmentoring.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val itemView = layoutInflater.inflate(R.layout.cell_longrid, parent, false)
        return GalleryAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is GalleryAdapterViewHolder) holder.bind()
    }

    override fun getItemCount(): Int {
        return audioCounter.size
    }

    class GalleryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(){

        }
    }
}