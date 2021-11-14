package net.d3b8g.vktestersmentoring.ui.login

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentLoginBinding
import net.d3b8g.vktestersmentoring.db.ConfData.ConfData
import net.d3b8g.vktestersmentoring.db.ConfData.ConfDatabase
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.helper.UITypes

class LoginFragment : Fragment(R.layout.fragment_login), LoginInterface {

    private val listBack: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var binding: FragmentLoginBinding

    private val userDatabase by lazy { UserDatabase.getInstance(requireContext()).userDatabaseDao}
    private val confDatabase by lazy { ConfDatabase.getInstance(requireContext()).confDatabaseDao }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)

        adapter = UserAdapter(this)
        binding.userDb.adapter = adapter
        binding.userDb.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.userDb.setHasFixedSize(true)
        updateUserData()

        binding.registerStart.setOnClickListener {
            binding.registerInput.let {
                if (!it.text.isNullOrEmpty() &&
                    it.text!!.length > 3 &&
                    it.text!!.contains(' ') &&
                    it.text!!.any { text -> text.isLetter() } &&
                    !listBack.any { user -> user.username == it.text!!.toString() }) {
                    val userId: Int = listBack.size + 1
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putInt("active_user_id", userId)
                        MainActivity.uid = userId
                    }
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            userDatabase.insert(UserData(
                                0, it.text!!.toString(), "https://sun9-4.userapi.com/impf/ory_hkWodVNvq5zVfv2D0gRUIPUT0KGCUGO9yQ/FOvsQmbn2e4.jpg?size=1499x1411&quality=95&sign=b2dbcc1151a49b3e01711f0cb36408bb&type=album", 0, 0
                            ))
                            confDatabase.insert(ConfData(
                                0, "null@vktm",""
                            ))
                        }
                    }
                    openUserUI()
                } else if (listBack.any { user -> user.username == it.text!!.toString() }) {
                    Toast.makeText(requireContext(), "Такой юзер уже есть", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Поле должно содержать Имя и Фамилию.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUserData() {
        lifecycleScope.launch {
            val listData = withContext(Dispatchers.IO) {
                return@withContext userDatabase.getAllData()
            }

            listBack.addAll(listData)

            if (!listBack.none()) {
                binding.userDb.visibility = View.VISIBLE
                adapter.setUser(listBack)
                binding.loginText.text = getString(R.string.login)
            }
        }
    }

    override fun loginUser(id: Int) {
        lifecycleScope.launch {
            val pass = getConfData(id).password
            if (pass.isNotEmpty()) {
                binding.tlInputPassword.visibility = View.VISIBLE
                binding.login.visibility = View.VISIBLE
                binding.login.setOnClickListener {
                    binding.loginPassword.let {
                        if (!it.text.isNullOrEmpty() && it.text.toString() == pass) {
                            id.saveLoginData()
                        } else {
                            binding.tlInputPassword.error = getString(R.string.wrong_password)
                        }
                    }
                }
            } else {
                id.saveLoginData()
            }
        }
    }

    private fun Int.saveLoginData() {
        PreferenceManager
            .getDefaultSharedPreferences(requireContext())
            .edit()
            .putInt("active_user_id", this)
            .apply()

        MainActivity.uid = this
        openUserUI()
    }

    private suspend fun getConfData(id: Int): ConfData = withContext(Dispatchers.IO) {
        return@withContext confDatabase.getUserById(id)
    }

    private fun openUserUI() {
        (requireActivity() as MainActivity).updateUI(UITypes.LOGIN_USER)
        val nav = LoginFragmentDirections.actionNavLoginToNavMain()
        findNavController().navigate(nav)
    }
}