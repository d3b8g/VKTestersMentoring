package net.d3b8g.vktestersmentoring.ui.MV

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.ui.MV.popup.UploadPhoto

class ModalViewA:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_mv,container,false)
        val mv_upload = inflate.findViewById<Button>(R.id.mv_upload)
        val mv_wv = inflate.findViewById<Button>(R.id.mv_wv)

        mv_upload.setOnClickListener {
            UploadPhoto(requireContext()).show()
        }

        return inflate
    }
}