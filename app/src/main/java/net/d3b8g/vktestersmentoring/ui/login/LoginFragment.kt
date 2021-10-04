package net.d3b8g.vktestersmentoring.ui.login

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
import net.d3b8g.vktestersmentoring.adapters.UserAdapter
import net.d3b8g.vktestersmentoring.databinding.FragmentLoginBinding
import net.d3b8g.vktestersmentoring.db.ConfData.ConfData
import net.d3b8g.vktestersmentoring.db.ConfData.ConfDatabase
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.interfaces.Login
import net.d3b8g.vktestersmentoring.modules.UITypes

class LoginFragment : Fragment(R.layout.fragment_login), Login {

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
                    var userId: Int
                    PreferenceManager.getDefaultSharedPreferences(context).apply {
                        userId = getInt("active_user_id", 0) + 1
                    }
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putBoolean("make_splash", true)
                        putInt("active_user_id", userId)
                    }
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            userDatabase.insert(UserData(
                                0, it.text!!.toString(), "", 0, 0
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
            withContext(Dispatchers.IO) {
                listBack.addAll(userDatabase.getAllData())
            }
        }

        if (!listBack.none()){
            binding.userDb.visibility = View.VISIBLE
            adapter.setUser(listBack)
            binding.loginText.text = getString(R.string.login)
        }
    }

    override fun loginUser(id: Int) {
        lifecycleScope.launch {
            val pass = getConfData(id).password
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
    }

    private suspend fun getConfData(id: Int): ConfData = withContext(Dispatchers.IO) {
        return@withContext confDatabase.getUserById(id)
    }

    private fun openUserUI() {
        val action = LoginFragmentDirections.actionNavLoginToNavHome2()
        findNavController().navigate(action)
        (requireActivity() as MainActivity).updateUI(UITypes.ALL_DATA)
    }
}