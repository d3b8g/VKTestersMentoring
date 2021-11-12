package net.d3b8g.vktestersmentoring.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentProfileBinding
import net.d3b8g.vktestersmentoring.db.ConfData.ConfData
import net.d3b8g.vktestersmentoring.db.ConfData.ConfDatabase
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.helper.ToolsShit.appLog
import net.d3b8g.vktestersmentoring.ui.gallery.Gallery.getGallerySize
import net.d3b8g.vktestersmentoring.ui.notes.Notes.getNotesAmount

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding

    private val userDatabase by lazy { UserDatabase.getInstance(requireContext()).userDatabaseDao }
    private val confBase by lazy { ConfDatabase.getInstance(requireContext()).confDatabaseDao }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProfileBinding.bind(view)

        with(binding) {

            profileHeader.run {
                lifecycleScope.launch {
                    val confData = getConfData()
                    if (confData.ident.length > 3 && confData.ident.substring(0,4) == "null") setTitleText("Профиль")
                    else setTitleText(confData.ident)
                }
                setRightButtonIcon(
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_settings_24, resources.newTheme())!!
                ){
                    val action = FragmentProfileDirections.actionNavProfileToNavSettings()
                    findNavController().navigate(action)
                }
            }

            lifecycleScope.launch {
                val user = getUser()
                // Hackerman!!!
                if (user.username.contains("<img src") ||
                    user.username.contains("<svg/") ||
                    user.username.contains("alert") ||
                    (user.username.contains("<") && user.username.contains(">"))) {
                    Picasso
                        .get()
                        .load("https://www.cloudav.ru/upload/iblock/331/pandasecurity-How-do-hackers-pick-their-targets.jpg")
                        .into(profileAvatar)
                } else Picasso.get().load(user.avatar).into(profileAvatar)

                profileUsername.text = user.username
                profileVisitsCount.text = "Вы зашли в приложение ${user.counter} ${titleStatus(user.counter)}"

            }
            val gallerySize = requireContext().getGallerySize()
            profileAudioCount.text = "$gallerySize ${titleStatus(gallerySize)} записали аудио в диктофоне"
            profileNotesCount.text = "Создал ${requireContext().getNotesAmount()} заметок"

            profileDirectConf.setOnClickListener {
                val nav = FragmentProfileDirections.actionNavProfileToNavConf()
                findNavController().navigate(nav)
            }
        }

        appLog(this, MainActivity.uid.toString())
    }

    private suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        return@withContext userDatabase.getUserById(MainActivity.uid)
    }

    private suspend fun getConfData(): ConfData = withContext(Dispatchers.IO) {
        return@withContext confBase.getUserById(MainActivity.uid)
    }

    private fun titleStatus(count: Int): String = when {
        count in 5..20 -> "раз"
        count.toString().takeLast(1) == "l" -> "1 раз"
        else -> {
            when (count.toString().takeLast(1).toInt()) {
                in 0..1 -> "раз"
                in 2..4 -> "раза"
                in 5..9 -> "раз"
                else -> "раз"
            }
        }
    }
}