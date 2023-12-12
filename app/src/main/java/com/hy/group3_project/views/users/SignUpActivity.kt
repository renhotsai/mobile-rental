package com.hy.group3_project.views.users

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.controllers.users.UserRepository
import com.hy.group3_project.databinding.ActivitySignUpBinding
import com.hy.group3_project.models.enums.Roles
import com.hy.group3_project.models.users.User


class SignUpActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var binding: ActivitySignUpBinding
//    private lateinit var adapter: UserAdapter
//    private var datasource:MutableList<User> = mutableListOf()

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)
        this.userRepository = UserRepository(applicationContext)
        auth = Firebase.auth
        binding.btnSignUp.setOnClickListener {
            signup()

        }
        binding.tvLogin.setOnClickListener {
            redirectLogin()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLogin) {
            redirectMain()
        }
    }

    private fun signup() {
//        val gson = Gson()
        val role = findViewById<RadioButton>(binding.rgRoles.checkedRadioButtonId).text.toString()
        val etFirstName = binding.etFirstName
        val etLastName = binding.etLastName
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        val etConfirmPassword = binding.etConfirmPassword
        val isReadPrivacy = binding.cbPrivacy.isChecked


        //check privacy
        if (!isReadPrivacy) {
            binding.cbPrivacy.error = "Please read the Privacy Policy"
            return
        }

        //check EditText is null or empty
        val editTextList = listOf(etFirstName, etLastName, etEmail, etPassword, etConfirmPassword)
        var hasEmptyValues = false
        for (et in editTextList) {
            if (et.text.toString().isNullOrEmpty()) {
                et.error = "Can not be Empty Value"
                hasEmptyValues = true
            }
        }
        if (hasEmptyValues) {
            return
        }

        //check email exist


        //check password == confirmPassword
        if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
            Toast.makeText(
                this@SignUpActivity,
                "Password & Confirm Password not same.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        //change string to enum
        val roleAsEnum: Roles = if (role == Roles.Landlord.toString()) {
            Roles.Landlord
        } else {
            Roles.Tenant
        }


        auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener { authResult ->
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = authResult.user
                val newUser = User(
                    user!!.uid,
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    roleAsEnum.toString(),
                )

                userRepository.addUserToDB(newUser)
                Log.d(TAG, "signup: signup succeed")
                Toast.makeText(this@SignUpActivity, "Success.", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { exception ->
                // If sign in fails, display a message to the user.
                Log.d(TAG, "createUserWithEmail:failure", exception)
                Toast.makeText(
                    baseContext,
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }






        Toast.makeText(this@SignUpActivity, "Success.", Toast.LENGTH_LONG)
        finish()
    }

}