package com.hy.group3_project.ViewActivities.Account

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Enums.LoginStatus
import com.hy.group3_project.Models.User
import com.hy.group3_project.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = sharedPreferences.edit()

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.tvSignUp.setOnClickListener{
            signUp()
        }
        binding.tvForgotPassword.setOnClickListener {
           sendForgotPassword()
        }
    }

    private fun sendForgotPassword() {

    }


    private fun signUp() {
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login() {
        //check empty values
        val editTexts = listOf<EditText>(binding.etEmail, binding.etPassword)
        var hasEmptyValues = false
        for (editText in editTexts) {
            if (editText.text.toString().isNullOrEmpty()) {
                editText.error = "Empty value."
                hasEmptyValues = true
            }
        }
        if (hasEmptyValues) {
            return
        }

        val userList = getUserList()
        //find email in user list
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val user = userList.find { it.email == email }
        if (user == null) {
            Toast.makeText(this@LoginActivity, "Email not registered", Toast.LENGTH_LONG).show()
            return
        }
        val loginStatus = user.login(email, password)
        if (loginStatus == LoginStatus.Success) {
            val gson = Gson()
            val userJson = gson.toJson(user)
            prefEditor.putString("KEY_USER", userJson)
            prefEditor.apply()

            Toast.makeText(this@LoginActivity, "Login $loginStatus", Toast.LENGTH_LONG).show()
            finish()
        } else {
            when (loginStatus) {
                LoginStatus.PasswordError -> {
                    Toast.makeText(this@LoginActivity, "Password Error", Toast.LENGTH_LONG).show()
                    return
                }

                else -> {
                    Toast.makeText(this@LoginActivity, "Unknown Error", Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
    }

    private fun getUserList(): MutableList<User> {
        var userList = mutableListOf<User>()
        val gson = Gson()
        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
        if (userListFromSP != null) {
            val typeToken = object : TypeToken<MutableList<User>>() {}.type
            userList = gson.fromJson<MutableList<User>>(userListFromSP, typeToken)
        }
        return userList
    }
}