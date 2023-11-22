package com.hy.group3_project.ViewActivities.Account

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.hy.group3_project.Models.User
import com.hy.group3_project.R
import com.hy.group3_project.databinding.ActivityMainBinding
import com.hy.group3_project.databinding.ActivityShowAcctBinding

class ShowAcctActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowAcctBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var perfEditor: SharedPreferences.Editor
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAcctBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.perfEditor = this.sharedPreferences.edit()

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


    private fun setAcctInfo() {
        checkLogin()
        if (user != null) {
            binding.tvEmail.text = user.email
            binding.tvFirstName.text = user.firstName
            binding.tvLastName.text = user.lastName
        }
    }
    private fun checkLogin() {
        val gson = Gson()
        val userFromSP = sharedPreferences.getString("KEY_USER", null)
        if (userFromSP != null) {
            user = gson.fromJson(userFromSP, User::class.java)
        }
    }

    override fun onResume() {
        setAcctInfo()
        invalidateOptionsMenu()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_after_login, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_item_edit_account_info -> {
                val intent = Intent(this@ShowAcctActivity, EditAcctInfoActivity::class.java)
                intent.putExtra("extra_user",user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_edit_password -> {
                val intent = Intent(this@ShowAcctActivity, EditPasswordActivity::class.java)
                intent.putExtra("extra_user",user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_logout -> {
                logout()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun logout() {
        perfEditor.remove("KEY_USER")
        perfEditor.apply()
        Toast.makeText(this@ShowAcctActivity, "Logout Success", Toast.LENGTH_LONG).show()
        invalidateOptionsMenu()
    }
}
