package net.d3b8g.vktestersmentoring.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.GalleryAdapter
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mediaTypeGallery
import net.d3b8g.vktestersmentoring.helper.PathHelper
import java.io.File

class GalleryFragment : Fragment() {

    lateinit var userImg:CircleImageView
    lateinit var userData:TextView
    lateinit var plug:TextView
    lateinit var rcv:RecyclerView
    lateinit var rcv_plug:LinearLayout
    lateinit var mediaType: AutoCompleteTextView

    lateinit var adapter: GalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        var titleType = root.findViewById<TextView>(R.id.fragment_gallery_title)

        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
        titleType.setOnClickListener { (requireActivity() as AppCompatActivity).onSupportNavigateUp() }
        userImg = root.findViewById(R.id.user_img_gallery)
        userData = root.findViewById(R.id.gallery_count)
        plug = root.findViewById(R.id.gallery_plug)
        rcv = root.findViewById(R.id.rcv_gallery)
        rcv_plug = root.findViewById(R.id.rcv_plug)

        val rpt = root.findViewById<TextView>(R.id.rcv_plug_text)
        val openDict = root.findViewById<TextView>(R.id.go_to_dictophone)

        userData.text = setupDataCount()

        mediaType = root.findViewById(R.id.media_type)
        mediaType.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.btn_shape))
        var adapterIssue = ArrayAdapter(requireActivity(),R.layout.list_item,mediaTypeGallery)
        mediaType.setAdapter(adapterIssue)
        mediaType.onItemClickListener = AdapterView.OnItemClickListener { _, _, index, id ->
            plug.visibility = View.GONE
            if(!adapter.update()) {
                rpt.text = when(id){
                    0.toLong() -> "Вы еще не записали ни одного аудио, хотите исправить это?"
                    1.toLong() -> "test text. Next release will replace this fragment on correct. Dont report this feature."
                    else -> "smth go wrong,Report"
                }
                rcv_plug.visibility = View.VISIBLE
            }
            else rcv.visibility = View.VISIBLE
        }
        mediaType.setOnClickListener { mediaType.showDropDown() }
        mediaType.showSoftInputOnFocus = false

        adapter = GalleryAdapter()
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(inflater.context, RecyclerView.HORIZONTAL,false)
        rcv.setHasFixedSize(true)

        openDict.setOnClickListener {

        }

        return root
    }

    private fun setupDataCount(): String {
        //bla-bla with WHEN param
        var count = File(PathHelper.audioPath).listFiles()?.size
        return when(true){
            else -> "У меня $count медиафайлов."
        }
    }
}