package net.d3b8g.vktestersmentoring.ui.uploads

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import net.d3b8g.vktestersmentoring.R


class UploadURL:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_upload, container, false)
        val url = root.findViewById<TextView>(R.id.upload_url)
        val img = root.findViewById<ImageView>(R.id.upload_image)
        val plug = root.findViewById<LinearLayout>(R.id.plug_attach)
        val tPlug = root.findViewById<TextInputLayout>(R.id.upload_l)

        url.setOnEditorActionListener { it, i, keyEvent ->
            if(i ==  EditorInfo.IME_ACTION_DONE){
                url.clearFocus()
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(url.windowToken, 0)
                Picasso.get().load(Uri.parse(url?.text.toString())).into(img)
                if(it.text.contains("https://") && it.text.split('/').size>3){
                    plug.visibility = View.GONE
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                        putString("user_img",it.text.toString())
                    }
                }
                else{
                    tPlug.error = "Инпут должен содержать ссылку."
                }
                true
            }
            false
        }

        return root
    }

    override fun onDestroy() {
        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity()?.currentFocus.windowToken, 0)
        super.onDestroy()

    }
}