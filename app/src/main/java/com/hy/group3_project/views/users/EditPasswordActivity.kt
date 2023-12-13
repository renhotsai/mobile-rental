package com.hy.group3_project.views.users

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityEditPasswordBinding
import com.hy.group3_project.models.enums.EditPasswordStatus
import com.hy.group3_project.models.users.User


class EditPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        val currentIntent = this@EditPasswordActivity.intent
        if (currentIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                user = currentIntent.getSerializableExtra("extra_user", User::class.java)!!
            } else {
                user = currentIntent.getSerializableExtra("extra_user") as User
            }
        }

        binding.btnSave.setOnClickListener {
            editPassword()
        }
    }

    private fun editPassword() {
        val etCurrPassword = binding.etCurrentPassword
        val etNewPassword = binding.etPassword
        val etConfirmPassword = binding.etConfirmPassword

        //check empty values
        val editTexts = listOf(etCurrPassword, etNewPassword, etConfirmPassword)
        var hasEmptyValues = false
        for (editText in editTexts) {
            if (editText.text.toString().isBlank()) {
                editText.error = "Can not be blank"
                hasEmptyValues = true
            }
        }
        if (hasEmptyValues) {
            return
        }

        val currPassword = etCurrPassword.text.toString()
        val newPassword = etNewPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        val firebaseUser = auth.currentUser
        val email = firebaseUser?.email
        val credential = EmailAuthProvider.getCredential(email!!, currPassword)
        Log.d(TAG, "editPassword: new:$newPassword, confirm:$confirmPassword")
        if (newPassword == confirmPassword) {
            firebaseUser?.reauthenticate(credential)
                ?.addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        firebaseUser.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(
                                        this@EditPasswordActivity,
                                        EditPasswordStatus.Success.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    redirectMain()
                                } else {
                                    Log.e(TAG, "update ${updateTask.exception?.message}")
                                    Snackbar.make(
                                        binding.root,
                                        updateTask.exception?.localizedMessage.toString(),
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                    cleanEditTextView(editTexts)
                                }
                            }
                    } else {
                        Log.w(TAG, "editPassword: reauth:${reauthTask.isSuccessful}")
                        Snackbar.make(
                            binding.root, "Current Password Wrong",
                            Snackbar.LENGTH_LONG
                        ).show()
                        cleanEditTextView(editTexts)
                    }
                }
        } else {
            Log.d(TAG, "editPassword: n != c")
            cleanEditTextView(editTexts)
        }
    }

    private fun cleanEditTextView(editTexts: List<EditText>) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }
}

