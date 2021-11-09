package net.d3b8g.vktestersmentoring.ui.uploads

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentUploadBinding
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.helper.ToolsShit.appLog
import net.d3b8g.vktestersmentoring.helper.UITypes
import net.d3b8g.vktestersmentoring.interfaces.UpdateMainUI


class UploadURL : Fragment(R.layout.fragment_upload) {

    lateinit var binding: FragmentUploadBinding
    var keyboardOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentUploadBinding.bind(view)

        with(binding) {

            uploadHeader.apply {
                setTitleText("Загрузка")
                setRightButtonIcon(
                    ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
                ){
                    findNavController().popBackStack()
                }
            }

            uploadUrl.setOnClickListener { keyboardOpen = true }

            uploadUrl.setOnEditorActionListener { it, i, _ ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    uploadUrl.clearFocus()
                    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(uploadUrl.windowToken, 0)
                    keyboardOpen = false
                    appLog(this@UploadURL, uploadUrl.text.toString())
                    Picasso.get().load(Uri.parse(uploadUrl.text.toString())).into(uploadImage)
                    if (it.text.contains("https://") && it.text.split('/').size > 3) {
                        uploadInputHint.error = null
                        plugAttach.visibility = View.GONE
                        val doUploadAvatar = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("do_avatar", false)
                        if (doUploadAvatar) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val bdUser = UserDatabase.getInstance(requireContext()).userDatabaseDao
                                bdUser.updateUserAvatar(it.text!!.toString())
                            }
                        }
                    }
                    else {
                        uploadInputHint.error = "Инпут должен содержать ссылку."
                    }

                    @Suppress("UNUSED_EXPRESSION")
                    true
                }
                false
            }
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