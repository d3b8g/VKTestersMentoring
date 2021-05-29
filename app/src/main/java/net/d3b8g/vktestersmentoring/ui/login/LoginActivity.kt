package net.d3b8g.vktestersmentoring.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.MainActivity.Companion.uid
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.UserAdapter
import net.d3b8g.vktestersmentoring.databinding.ActivityLoginBinding
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_avatar
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_counter
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_scope
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_uid
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_username
import net.d3b8g.vktestersmentoring.interfaces.Login
import net.d3b8g.vktestersmentoring.modules.UserConfData
import net.d3b8g.vktestersmentoring.modules.UserData

class LoginActivity : AppCompatActivity(), Login {

    private val listBack:ArrayList<UserData> = ArrayList()
    lateinit var adapter:UserAdapter
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if(getBoolean("make_splash", false)) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(this)
        binding.userDb.adapter = adapter
        binding.userDb.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.userDb.setHasFixedSize(true)
        updateUserData()
        binding.registerStart.setOnClickListener {
            binding.registerInput.let {
                if(!it.text.isNullOrEmpty() &&
                    it.text!!.length>3 &&
                    it.text!!.contains(' ') &&
                    it.text!!.any { text -> text.isLetter() } &&
                    !listBack.any { user -> user.username == it.text!!.toString() }) {
                        var user_id:Int
                        PreferenceManager.getDefaultSharedPreferences(this).apply {
                            user_id = getInt("active_user_id", 0) + 1
                        }
                        PreferenceManager.getDefaultSharedPreferences(this).edit {
                            putBoolean("make_splash", true).apply()
                            putInt("active_user_id",user_id)
                        }
                        CreateUserExist(this@LoginActivity).insertData(
                            UserData(
                                id = 0,
                                username = it.text!!.toString(),
                                avatar = "https://sun9-67.userapi.com/impg/I8qae64Ppm2JRUm4E_ioXR7rgSpfLY81K02nUg/XN3Fn9zsP5g.jpg?size=726x612&quality=96&proxy=1&sign=6412c55a2d2a6b5c06c31ca6de71aab1",
                                scope = 0,
                                counter = 0
                            ),
                            UserConfData(
                                id = 0,
                                login = "null@vktm",
                                password = ""
                            ), this@LoginActivity)
                        startActivity(Intent(this,MainActivity::class.java))
                } else if(listBack.any { user -> user.username == it.text!!.toString() }) {
                    Toast.makeText(this, "Такой юзер уже есть", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Поле должно содержать Имя и Фамилию.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateUserData() {
        val db = CreateUserExist(this).readableDatabase
        val query = "Select * from ${CreateUserExist.table_name[0]}"
        try {
            val result = db.rawQuery(query, null)
            if (result.moveToFirst()) {
                do{
                    listBack.add(UserData(
                        id = result.getString(result.getColumnIndex(col_uid)).toInt(),
                        username = result.getString(result.getColumnIndex(col_username)),
                        avatar = result.getString(result.getColumnIndex(col_avatar)),
                        scope = result.getString(result.getColumnIndex(col_scope)).toInt(),
                        counter = result.getString(result.getColumnIndex(col_counter)).toInt()
                    ))
                }while (result.moveToNext())
            }
        }catch (e: Exception) {
            e.stackTrace
        }

        if(!listBack.none()){
            binding.userDb.visibility = View.VISIBLE
            adapter.setUser(listBack)
            binding.loginText.text = getString(R.string.login)
        }
    }

    override fun loginUser(id: Int) {
        val pass = CreateUserExist(this).readConfData(id)!!.password
        if(pass.isNotEmpty()) {
            binding.tlInputPassword.visibility = View.VISIBLE
            binding.login.visibility = View.VISIBLE
            binding.login.setOnClickListener {
                binding.loginPassword.let {
                    if(!it.text.isNullOrEmpty() && it.text.toString() == pass) {
                        PreferenceManager.getDefaultSharedPreferences(this).edit {
                            putBoolean("make_splash", true).apply()
                            putInt("active_user_id",id)
                        }
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }else{
                        binding.tlInputPassword.error = getString(R.string.wrong_password)
                    }
                }
            }
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean("make_splash", true).apply()
                putInt("active_user_id",id)
            }
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        if(uid == 1) super.onBackPressed()
        else Toast.makeText(this, "Необходимо авторизоваться", Toast.LENGTH_SHORT).show()
    }

}