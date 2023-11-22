package com.hy.group3_project.ViewActivities.Account

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Enums.Roles
import com.hy.group3_project.Models.User
import com.hy.group3_project.R
import com.hy.group3_project.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = sharedPreferences.edit()

        binding.btnSignUp.setOnClickListener {
            signup()
        }
        binding.tvLogin.setOnClickListener {
            login()
        }
    }


    private fun login() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signup() {
        val gson = Gson()
        val role = findViewById<RadioButton>(binding.rgRoles.checkedRadioButtonId).text.toString()
        val etFirstName = binding.etFirstName
        val etLastName = binding.etLastName
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        val etConfirmPassword = binding.etConfirmPassword
        val isReadPrivacy = binding.cbPrivacy.isChecked

        //check privacy
        if(!isReadPrivacy){
            binding.cbPrivacy.error = "Please read the Privacy Policy"
            return
        }

        //check EditText is null or empty
        val editTextList = listOf(etFirstName, etLastName, etEmail, etPassword, etConfirmPassword)
        var hasEmptyValues = false
        for(et in editTextList){
            if(et.text.toString().isNullOrEmpty()){
                et.error = "Can not be Empty Value"
                hasEmptyValues = true
            }
        }
        if (hasEmptyValues) {
            return
        }

        //check email exist
        var userList:MutableList<User> = mutableListOf()
        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
        if(userListFromSP != null){
            val typeToken = object : TypeToken<MutableList<User>>() {}.type
            userList = gson.fromJson<MutableList<User>>(userListFromSP, typeToken)
            val user = userList.find {
                it.email == etEmail.text.toString()
            }
            if (user != null) {
                Toast.makeText(this@SignUpActivity,"Email exist.", Toast.LENGTH_LONG).show()
                return
            }
        }

        //check password == confirmPassword
        if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
            Toast.makeText(this@SignUpActivity,"Password & Confirm Password not same.", Toast.LENGTH_LONG).show()
            return
        }

        //change string to enum
        val roleAsEnum: Roles = if (role == Roles.Landlords.toString()) {
            Roles.Landlords
        }else{
            Roles.Tenants
        }

        val newUser = User(
            etFirstName.text.toString(),
            etLastName.text.toString(),
            etEmail.text.toString(),
            roleAsEnum,
            etPassword.text.toString(),
        )
        userList.add(newUser)

        // storage list
        val userListJson = gson.toJson(userList)
        prefEditor.putString("KEY_USERLIST",userListJson)

        // storage user
        newUser.login(etEmail.text.toString(), etPassword.text.toString())
        val userJson = gson.toJson(newUser)
        prefEditor.putString("KEY_USER",userJson)
        prefEditor.apply()

        Toast.makeText(this@SignUpActivity,"Success.", Toast.LENGTH_LONG)
        finish()
    }
}