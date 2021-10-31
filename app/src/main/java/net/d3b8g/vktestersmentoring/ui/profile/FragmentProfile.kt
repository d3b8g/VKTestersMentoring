package net.d3b8g.vktestersmentoring.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.customUI.fragmentHeader.FragmentHeader
import net.d3b8g.vktestersmentoring.databinding.FragmentProfileBinding
import net.d3b8g.vktestersmentoring.db.ConfData.ConfData
import net.d3b8g.vktestersmentoring.db.ConfData.ConfDatabase
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.ui.gallery.Gallery.getGallerySize

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding
    val fragmentHeader: FragmentHeader by lazy {
        binding.profileHeader
    }

    private val userDatabase by lazy { UserDatabase.getInstance(requireContext()).userDatabaseDao }
    private val confBase by lazy { ConfDatabase.getInstance(requireContext()).confDatabaseDao }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProfileBinding.bind(view)

        fragmentHeader.apply {
            lifecycleScope.launch {
                val confData = getConfData()
                if (confData.ident.substring(0,4) == "null") setTitleText("Профиль")
                else setTitleText(confData.ident.substring(0,4))
            }
            setRightButtonIcon(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_settings_24, resources.newTheme())!!
            )
            setRightButtonListener {
                val action = FragmentProfileDirections.actionNavProfileToNavSettings()
                findNavController().navigate(action)
            }
        }

        val avatar = binding.profileAvatar
        val username = binding.profileUsername

        lifecycleScope.launch {
            val user = getUser()
            Picasso.get().load(user.avatar).into(avatar)
            username.text = user.username
            binding.profileVisitsCount.text = "Вы зашли в приложение ${user.counter} ${titleStatus(user.counter)}"
        }
        val gallerySize = requireContext().getGallerySize()
        binding.profileAudioCount.text = "$gallerySize ${titleStatus(gallerySize)} записали аудио в диктофоне"
    }

    private suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        return@withContext userDatabase.getUserById(MainActivity.uid)
    }

    private suspend fun getConfData(): ConfData = withContext(Dispatchers.IO) {
        return@withContext confBase.getUserById(MainActivity.uid)
    }

    private fun titleStatus(count: Int?): String = when {
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