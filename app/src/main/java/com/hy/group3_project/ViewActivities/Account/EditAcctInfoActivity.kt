package com.hy.group3_project.ViewActivities.Account

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Enums.EditAccountStatus
import com.hy.group3_project.Models.User
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityEditAcctInfoBinding


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

        setEditTextHint(user)

        //set click action
        binding.btnSave.setOnClickListener {
            editAccountInfo()
        }
    }

    private fun setEditTextHint(user: User) {
        binding.etEmail.hint = user.email
        binding.etFirstName.hint = user.firstName
        binding.etLastName.hint = user.lastName
    }
    private fun editAccountInfo() {
        val etFirstName = binding.etFirstName.text.toString()
        val etLastName = binding.etLastName.text.toString()
        val etEmail = binding.etEmail.text.toString()

        val gson = Gson()
        // find user list
        val userList = getUserList()
        //find user in user list
        user = userList.find { it.email == user.email }!!

        //update user in user list
        val changeAcctInfoStatus =
            user.changeAcctInfo(etFirstName, etLastName, etEmail)
        if (changeAcctInfoStatus == EditAccountStatus.Success) {
            Toast.makeText(
                this@EditAcctInfoActivity,
                changeAcctInfoStatus.toString(),
                Toast.LENGTH_LONG
            ).show()

            val userJson = gson.toJson(user)
            prefEditor.putString("KEY_USER", userJson)

            val userListJson = gson.toJson(userList)
            prefEditor.putString("KEY_USERLIST", userListJson)
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