package net.d3b8g.vktestersmentoring.ui.MV

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentMvBinding
import net.d3b8g.vktestersmentoring.ui.uploads.UploadPhoto

class ModalViewA : Fragment(R.layout.fragment_mv) {

    lateinit var binding: FragmentMvBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMvBinding.bind(view)

        binding.mvHeader.apply {
            setTitleText("Модальные окна")
            setRightButtonIcon(
                ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
            ){
                findNavController().popBackStack()
            }
        }
        binding.mvUpload.setOnClickListener {
            UploadPhoto(requireContext()).show()
        }

    }

}