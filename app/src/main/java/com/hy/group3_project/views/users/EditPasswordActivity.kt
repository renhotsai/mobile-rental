package com.hy.group3_project.views.users

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.hy.group3_project.models.enums.EditPasswordStatus
import com.hy.group3_project.models.users.User
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityEditPasswordBinding


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
            if (editText.text.toString().isNullOrEmpty()) {
                editText.error = "Empty value"
                hasEmptyValues = true
            }
        }
        if (hasEmptyValues) {
            return
        }

        val gson = Gson()
        // find user list
        val userList = getUserList()
        //find user in user list
        user = userList.find { it.id == user!!.id }!!

        val changePasswordStatus = user!!.changePassword(
            etCurrPassword.text.toString(),
            etNewPassword.text.toString(),
            etConfirmPassword.text.toString()
        )
        if (changePasswordStatus == EditPasswordStatus.Success) {
            val userJson = gson.toJson(user)
            prefEditor.putString("KEY_USER", userJson)

            val userListJson = gson.toJson(userList)
            prefEditor.putString("KEY_USERLIST", userListJson)

            prefEditor.apply()
            Toast.makeText(
                this@EditPasswordActivity, "$changePasswordStatus", Toast.LENGTH_LONG
            ).show()
            finish()
        } else {
            Toast.makeText(
                this@EditPasswordActivity, "Error:$changePasswordStatus", Toast.LENGTH_LONG
            ).show()
            cleanEditTextView(editTexts)
        }
    }

    private fun cleanEditTextView(editTexts: List<EditText>) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }
}

