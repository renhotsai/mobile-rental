package com.hy.group3_project.views.users

import android.os.Build
import android.os.Bundle
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityShowAcctBinding
import com.hy.group3_project.models.users.User

class ShowAcctActivity : BaseActivity() {
    private lateinit var binding: ActivityShowAcctBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAcctBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        val currentIntent = this@ShowAcctActivity.intent
        if (currentIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                user = currentIntent.getSerializableExtra("extra_user", User::class.java)!!
            } else {
                user = currentIntent.getSerializableExtra("extra_user") as User
            }
        }

        setAcctInfo()
    }
    override fun onResume() {
        super.onResume()
        setAcctInfo()
    }
    private fun setAcctInfo() {
        checkLogin()
        if (user != null) {
//            binding.tvEmail.text = user!!.email
            binding.tvFirstName.text = user!!.firstName
            binding.tvLastName.text = user!!.lastName
        }
    }
}
