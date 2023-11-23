package com.hy.group3_project.ViewActivities.Account

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.hy.group3_project.Enums.LoginStatus
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.tvSignUp.setOnClickListener{
            redirectSignUp()
            finish()
        }
        binding.tvForgotPassword.setOnClickListener {
           sendForgotPassword()
        }
    }

    override fun onResume() {
        super.onResume()
        if(isLogin){
            redirectMain()
        }
    }
    private fun sendForgotPassword() {

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
}