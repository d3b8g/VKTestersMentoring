package net.d3b8g.vktestersmentoring.ui.confdata

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import net.d3b8g.vktestersmentoring.MainActivity.Companion.uid
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentUserdataBinding
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import net.d3b8g.vktestersmentoring.modules.UserConfData


class UserDataFragment : Fragment(R.layout.fragment_userdata) {
    private lateinit var binding: FragmentUserdataBinding

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        binding = FragmentUserdataBinding.bind(root)

        CreateUserExist(root.context).readConfData(uid)?.login?.apply {
            if (this != "null@vktm") {
                binding.confUident.text = this
                binding.confIdentTl.visibility = View.GONE
                binding.confPasswordTl.visibility = View.GONE
                binding.confSave.isClickable = false
            }
        }

        binding.confSave.setOnClickListener {
            if (binding.confPassword.text.toString().length in 2..4) {
                if (binding.confIdent.text.toString().length > 2 && binding.confIdent.text.toString().matches("^\\d+\$".toRegex())) {
                    saveUserConf(root.context)
                }
                else binding.confIdentTl.error = "Формат записи: Int и больше 2 цифр"
            }
            else binding.confPasswordTl.error = "Допустимо от 2 до 4 символов"
        }
    }


    private fun genPass(ct: Context): String {
        CreateUserExist(ct).readUserData(uid)!!.username.apply {
            var username = ""
            this.split(' ').forEachIndexed { index, un ->
                username += when(index){
                    0 -> un
                    1 -> binding.confIdent.text.toString().replace("@vktm", "") + un
                    else -> null
                }
            }
            val str = Base64.encodeToString(username.reversed().toByteArray(), Base64.DEFAULT).replace(
                "=",
                ""
            ).reversed()
            val randomKey = (str.indices-1).random()
            val std = Base64.encodeToString(
                (str.replace(
                    str.substring(randomKey,randomKey+1),
                    binding.confPassword.text.toString()
                ) + randomKey).toByteArray(), Base64.DEFAULT
            ).replace("=", "")
            val stb = (std.substring(0, (std.length - 1) / 2).reversed()
                    + std.substring((std.length - 1) / 2 + 1, std.length - 1)).reversed()
            return stb.map { it.initNumber() }.joinToString("")
        }
    }

    private fun Char.initNumber() : String {
        return if(this.isDigit()) {
            when(this) {
                '0' -> "zt"
                '1' -> "gt"
                '2' -> "fr"
                '3' -> "gy"
                '4' -> "as"
                '5' -> "ts"
                '6' -> "pu"
                '7' -> "nb"
                '8' -> "vw"
                '9' -> "as"
                else -> "!WARN!"
            } + "7$this"
        } else this.toString()
    }

    private fun saveUserConf(ct: Context) {
        try {
            val passwordGen = genPass(ct)
            val updateUserConf = CreateUserExist(ct).writeConfData(
                UserConfData(
                    id = uid,
                    login = binding.confIdent.text.toString(),
                    password = passwordGen
                )
            )
            if(updateUserConf) {
                binding.confUident.text = binding.confIdent.text.toString()
                binding.confIdentTl.visibility = View.GONE
                binding.confPasswordTl.visibility = View.GONE
                binding.confSave.isClickable = false

                val clipboard = ct.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Password Of VKTM", passwordGen)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(ct, "Пароль скопирован в буффер", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            e.stackTrace
        }
    }

}