package com.hy.group3_project.ViewActivities.Account

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.hy.group3_project.Models.User
import com.hy.group3_project.R
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityShowAcctBinding

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
            binding.tvEmail.text = user.email
            binding.tvFirstName.text = user.firstName
            binding.tvLastName.text = user.lastName
        }
    }
}
