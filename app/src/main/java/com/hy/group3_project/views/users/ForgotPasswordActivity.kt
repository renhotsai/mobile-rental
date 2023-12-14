package com.hy.group3_project.views.users

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendEmail.setOnClickListener {
            if (binding.etEmail.text.isBlank()) {
                binding.etEmail.error = "Email can not be blank"
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(binding.etEmail.text.toString())
                .addOnCompleteListener { task ->
                    run {
                        if (task.isSuccessful) {
                            Log.d(TAG, "Reset Password Email Sent.")
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Reset Password Email Sent.",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }else{
                            Log.d(TAG, task.exception!!.localizedMessage)
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "${task.exception!!.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
        }
    }

}