package com.hy.group3_project.views.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch


class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
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
        binding.tvSignUp.setOnClickListener {
            redirectSignUp()
            finish()
        }
        binding.tvForgotPassword.setOnClickListener {
            sendForgotPassword()
        }
    }

    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            redirectMain()
        }
    }

    private fun sendForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
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

        //find email in user list
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                lifecycleScope.launch {
                    user = userRepository.findUser(authResult.user!!.uid)
                    prefEditorUser(user!!)
                    afterLoginAndSignup()
                }
                Toast.makeText(this, "Success!!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                // Sign in fails, displays a message to the user
                Log.e(TAG, "signInWithEmail:failure", exception)
                Toast.makeText(this, exception.message.toString(), Toast.LENGTH_LONG).show()
            }
    }
}