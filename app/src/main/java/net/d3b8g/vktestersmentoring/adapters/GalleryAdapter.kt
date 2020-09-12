package net.d3b8g.vktestersmentoring.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R

class GalleryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_longrid, parent, false)
        return GalleryAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is GalleryAdapterViewHolder) holder.bind()
    }

    override fun getItemCount(): Int {
        return 4
    }

    class GalleryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(){

        }
    }
}