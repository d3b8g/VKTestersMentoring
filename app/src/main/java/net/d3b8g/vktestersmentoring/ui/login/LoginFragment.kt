package net.d3b8g.vktestersmentoring.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.UserAdapter
import net.d3b8g.vktestersmentoring.databinding.FragmentLoginBinding
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_avatar
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_counter
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_scope
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_uid
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_username
import net.d3b8g.vktestersmentoring.interfaces.Login
import net.d3b8g.vktestersmentoring.modules.UserConfData
import net.d3b8g.vktestersmentoring.modules.UserData

class LoginFragment : Fragment(R.layout.fragment_login), Login {

    private val listBack: ArrayList<UserData> = ArrayList()
    lateinit var adapter: UserAdapter
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)

        adapter = UserAdapter(this)
        binding.userDb.adapter = adapter
        binding.userDb.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.userDb.setHasFixedSize(true)
        updateUserData()

        binding.registerStart.setOnClickListener {
            binding.registerInput.let {
                if(!it.text.isNullOrEmpty() &&
                    it.text!!.length > 3 &&
                    it.text!!.contains(' ') &&
                    it.text!!.any { text -> text.isLetter() } &&
                    !listBack.any { user -> user.username == it.text!!.toString() }) {
                    var userId: Int
                    PreferenceManager.getDefaultSharedPreferences(context).apply {
                        userId = getInt("active_user_id", 0) + 1
                    }
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putBoolean("make_splash", true)
                        putInt("active_user_id", userId)
                    }
                    CreateUserExist(requireContext()).insertData(
                        UserData(0, it.text!!.toString(), "", 0, 0),
                        UserConfData(0, "null@vktm",""), requireContext())
                    openUserUI()
                } else if(listBack.any { user -> user.username == it.text!!.toString() }) {
                    Toast.makeText(requireContext(), "Такой юзер уже есть", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Поле должно содержать Имя и Фамилию.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateUserData() {
        val db = CreateUserExist(requireContext()).readableDatabase
        val query = "Select * from ${CreateUserExist.table_name[0]}"
        try {
            val result = db.rawQuery(query, null)
            if (result.moveToFirst()) {
                do {
                    listBack.add(UserData(
                        id = result.getString(result.getColumnIndex(col_uid)).toInt(),
                        username = result.getString(result.getColumnIndex(col_username)),
                        avatar = result.getString(result.getColumnIndex(col_avatar)),
                        scope = result.getString(result.getColumnIndex(col_scope)).toInt(),
                        counter = result.getString(result.getColumnIndex(col_counter)).toInt()
                    ))
                } while (result.moveToNext())
            }
        } catch (e: Exception) {
            e.stackTrace
        }

        if(!listBack.none()){
            binding.userDb.visibility = View.VISIBLE
            adapter.setUser(listBack)
            binding.loginText.text = getString(R.string.login)
        }
    }

    override fun loginUser(id: Int) {
        val pass = CreateUserExist(requireContext()).readConfData(id)!!.password
        if(pass.isNotEmpty()) {
            binding.tlInputPassword.visibility = View.VISIBLE
            binding.login.visibility = View.VISIBLE
            binding.login.setOnClickListener {
                binding.loginPassword.let {
                    if(!it.text.isNullOrEmpty() && it.text.toString() == pass) {
                        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                            putBoolean("make_splash", true)
                            putInt("active_user_id", id)
                        }
                        openUserUI()
                    } else {
                        binding.tlInputPassword.error = getString(R.string.wrong_password)
                    }
                }
            }
        } else {
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                putBoolean("make_splash", true)
                putInt("active_user_id", id)
            }
            openUserUI()
        }
    }

    private fun openUserUI() {
        val action = LoginFragmentDirections.actionNavLoginToNavHome2()
        findNavController().navigate(action)
    }
}