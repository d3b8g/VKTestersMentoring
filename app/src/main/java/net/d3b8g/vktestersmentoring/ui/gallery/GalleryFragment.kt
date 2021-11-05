package net.d3b8g.vktestersmentoring.ui.gallery

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.customUI.fragmentHeader.FragmentHeader
import net.d3b8g.vktestersmentoring.databinding.FragmentSlideshowBinding
import net.d3b8g.vktestersmentoring.ui.gallery.Gallery.getGallerySize

class GalleryFragment : Fragment(R.layout.fragment_slideshow) {

    private lateinit var adapter: GalleryAdapter
    private lateinit var binding: FragmentSlideshowBinding
    private val fragmentHeader: FragmentHeader by lazy {
        binding.bugsHeader
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        binding = FragmentSlideshowBinding.bind(root)

        PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
            Picasso
                .get()
                .load("https://sun9-31.userapi.com/impf/c845017/v845017958/16cb40/BcXfWFsRUCw.jpg?size=1920x1280&quality=96&proxy=1&sign=7592ba24714b362951ee433338fee195")
                .into(binding.userImgGallery)
        }

        adapter = GalleryAdapter()
        val typeListGallery = listOf("Аудиозаписи")
        val adapterPopup = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, typeListGallery)
        val listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle).apply {
            anchorView = binding.mediaType
            setAdapter(adapterPopup)

            setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                binding.galleryPlug.visibility = View.GONE
                if (!adapter.updateGalleryAdapter(requireContext())) {
                    binding.rcvPlugText.text = when(position) {
                        0 -> "Вы еще не записали ни одного аудио, хотите исправить это?"
                        in 1..2 -> "Тестовая кнопка"
                        else -> "smth go wrong,Report"
                    }
                    binding.rcvPlug.visibility = View.VISIBLE
                }
                else binding.galleryPlug.visibility = View.VISIBLE

                binding.mediaType.text = typeListGallery[0]
                dismiss()
            }
        }

        binding.galleryCount.text = setupDataCount()

        binding.mediaType.apply {
            text = typeListGallery[0]
            setOnClickListener {
                listPopupWindow.show()
            }
        }

        binding.rcvGallery.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
            setHasFixedSize(false)
        }
        adapter.updateGalleryAdapter(requireContext())

        fragmentHeader.apply {
            setTitleText("Галерея")
            setRightButtonIcon(
                ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
            )
            setRightButtonListener {
                findNavController().popBackStack()
            }
        }

        binding.goToDictophone.setOnClickListener {
            val action = GalleryFragmentDirections.actionNavSlideshowToNavDictophone()
            findNavController().navigate(action)
        }
    }

    private fun setupDataCount(): String {
        var count = requireContext().getGallerySize()
        return if (count in 5..20) {
            "У меня $count медиафайлов"
        } else {
            when(count.toString().takeLast(1).toInt()) {
                0 -> "У меня $count медиафайлов"
                1 -> "У меня $count медиафайл"
                in 2..4 -> "У меня $count медиафайла"
                in 5..9 -> "У меня $count медиафайлов"
                else -> "ERROR"
            }
        }
    }
}