package net.d3b8g.vktestersmentoring.ui.uploads

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.squareup.picasso.Picasso
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentUploadBinding
import net.d3b8g.vktestersmentoring.helper.UITypes
import net.d3b8g.vktestersmentoring.interfaces.UpdateMainUI


class UploadURL : Fragment(R.layout.fragment_upload) {

    lateinit var binding: FragmentUploadBinding
    var keyboardOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentUploadBinding.bind(view)

        val url = binding.uploadUrl
        val img = binding.uploadImage
        val plug = binding.plugAttach
        val tPlug = binding.uploadL

        url.setOnClickListener { keyboardOpen = true }

        url.setOnEditorActionListener { it, i, _ ->
            if (i ==  EditorInfo.IME_ACTION_DONE) {
                url.clearFocus()
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(url.windowToken, 0)
                keyboardOpen = false
                Picasso.get().load(Uri.parse(url.text.toString())).into(img)
                if (it.text.contains("https://") && it.text.split('/').size > 3) {
                    tPlug.error = null
                    plug.visibility = View.GONE
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                        putString("user_img",it.text.toString())
                    }
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
                        if (getBoolean("do_avatar",false)) {
                            (requireActivity() as UpdateMainUI).updateUI(UITypes.AVATAR)
                        }
                    }
                }
                else {
                    tPlug.error = "Инпут должен содержать ссылку."
                }

                @Suppress("UNUSED_EXPRESSION")
                true
            }
            false
        }
    }

    override fun onDestroy() {
        if (keyboardOpen) {
            val inputMethodManager =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                0
            )
        }
        super.onDestroy()
    }
}