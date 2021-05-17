package net.d3b8g.vktestersmentoring.ui.gallery

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
    lateinit var dictoOpen: Button

    lateinit var adapter: GalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        userImg = root.findViewById(R.id.user_img_gallery)
        userData = root.findViewById(R.id.gallery_count)
        plug = root.findViewById(R.id.gallery_plug)
        rcv = root.findViewById(R.id.rcv_gallery)
        rcv_plug = root.findViewById(R.id.rcv_plug)
        dictoOpen = root.findViewById(R.id.go_to_dictophone)

        PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
            Picasso.get().load(getString("user_img","https://sun9-31.userapi.com/impf/c845017/v845017958/16cb40/BcXfWFsRUCw.jpg?size=1920x1280&quality=96&proxy=1&sign=7592ba24714b362951ee433338fee195")).into(userImg)
        }

        val rpt = root.findViewById<TextView>(R.id.rcv_plug_text)

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

        dictoOpen.setOnClickListener {

        }

        return root
    }

    private fun setupDataCount(): String {
        //bla-bla with WHEN param
        var count = File(PathHelper.audioPath).listFiles()?.size
        return if(count == null){
            "У меня 0 медиафайлов"
        }else{
            if(count in 5..20) {
                "У меня $count медиафайлов"
            }else{
                when (count.toString().takeLast(1).toInt()) {
                    0 -> "У меня $count медиафайлов"
                    1 -> "У меня $count медиафайл"
                    in 2..4 -> "У меня $count медиафайла"
                    in 5..9 -> "У меня $count медиафайлов"
                    else -> "ERROR"
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
    }
}