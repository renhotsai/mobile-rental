package com.hy.group3_project.views.users

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.gson.Gson
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var binding: ActivityLoginBinding
//    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)
        auth = Firebase.auth

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

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                // Sign in success, update UI with signed-in user's information
                Log.d(TAG, "login successful")
                val gson = Gson()
                val userJson = gson.toJson(user)
                prefEditor.putString("KEY_USER", userJson)
                prefEditor.apply()

                Log.d(TAG, "isLogin first: $isLogin")
                finish()
            }
            .addOnFailureListener { exception ->
                // Sign in fails, displays a message to the user
                Log.e(TAG, "signInWithEmail:failure", exception)
            }

//        val user = userList.find { it.email == email }
//        if (user == null) {
//            Toast.makeText(this@LoginActivity, "Email not registered", Toast.LENGTH_LONG).show()
//            return
//        }
//        val loginStatus = user.login(email, password)
//        if (loginStatus == LoginStatus.Success) {
//            val gson = Gson()
//            val userJson = gson.toJson(user)
//            prefEditor.putString("KEY_USER", userJson)
//            prefEditor.apply()
//
//            Toast.makeText(this@LoginActivity, "Login $loginStatus", Toast.LENGTH_LONG).show()
//            finish()
//        } else {
//            when (loginStatus) {
//                LoginStatus.PasswordError -> {
//                    Toast.makeText(this@LoginActivity, "Password Error", Toast.LENGTH_LONG).show()
//                    return
//                }
//
//                else -> {
//                    Toast.makeText(this@LoginActivity, "Unknown Error", Toast.LENGTH_LONG).show()
//                    return
//                }
//            }
//        }
    }
}