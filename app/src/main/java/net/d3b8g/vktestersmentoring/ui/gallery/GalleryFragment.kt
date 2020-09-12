package net.d3b8g.vktestersmentoring.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.GalleryAdapter

class GalleryFragment : Fragment() {

    lateinit var userImg:CircleImageView
    lateinit var userData:TextView
    lateinit var audio:Button
    lateinit var plug:Button
    lateinit var rcv:RecyclerView

    lateinit var adapter: GalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        userImg = root.findViewById(R.id.user_img_gallery)
        userData = root.findViewById(R.id.gallery_count)
        audio = root.findViewById(R.id.open_gallery_dicto)
        plug = root.findViewById(R.id.gallery_plug)
        rcv = root.findViewById(R.id.rcv_gallery)

        adapter = GalleryAdapter()
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(inflater.context, RecyclerView.HORIZONTAL,false)
        rcv.setHasFixedSize(true)

        return root
    }
}