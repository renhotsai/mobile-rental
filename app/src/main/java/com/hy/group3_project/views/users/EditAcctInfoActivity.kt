package com.hy.group3_project.views.users

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityEditAcctInfoBinding
import com.hy.group3_project.models.enums.EditAccountStatus
import com.hy.group3_project.models.users.User


class EditAcctInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityEditAcctInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAcctInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        val currentIntent = this@EditAcctInfoActivity.intent
        if (currentIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                user = currentIntent.getSerializableExtra("extra_user", User::class.java)!!
            } else {
                user = currentIntent.getSerializableExtra("extra_user") as User
            }
        }

        setEditTextHint(user!!)

        //set click action
        binding.btnSave.setOnClickListener {
            editAccountInfo()
        }
    }

    private fun setEditTextHint(user: User) {
//        binding.etEmail.hint = user.email
        binding.etFirstName.hint = user.firstName
        binding.etLastName.hint = user.lastName
    }
    private fun editAccountInfo() {
        val etFirstName = binding.etFirstName.text.toString()
        val etLastName = binding.etLastName.text.toString()
        val etEmail = binding.etEmail.text.toString()

        val gson = Gson()

        //update user in user list
        val changeAcctInfoStatus =
            user!!.changeAcctInfo(etFirstName, etLastName, etEmail)
        if (changeAcctInfoStatus == EditAccountStatus.Success) {
            Toast.makeText(
                this@EditAcctInfoActivity,
                changeAcctInfoStatus.toString(),
                Toast.LENGTH_LONG
            ).show()

            val userJson = gson.toJson(user)
            prefEditor.putString("KEY_USER", userJson)

            prefEditor.apply()
            finish()
        }else{
            Toast.makeText(
                this@EditAcctInfoActivity,
                changeAcctInfoStatus.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}