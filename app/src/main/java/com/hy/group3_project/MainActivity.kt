package com.hy.group3_project

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.hy.group3_project.Models.User
import com.hy.group3_project.ViewActivities.Account.EditAcctInfoActivity
import com.hy.group3_project.ViewActivities.Account.EditPasswordActivity
import com.hy.group3_project.ViewActivities.Account.LoginActivity
import com.hy.group3_project.ViewActivities.Account.ShowAcctActivity
import com.hy.group3_project.ViewActivities.Account.SignUpActivity
import com.hy.group3_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var perfEditor: SharedPreferences.Editor
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.perfEditor = this.sharedPreferences.edit()

        setSupportActionBar(this.binding.tbOptionMenu)
        checkLogin()
    }

    override fun onResume() {
        checkLogin()
        invalidateOptionsMenu()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val userFromSP = sharedPreferences.getString("KEY_USER", null)
        if (userFromSP == null) {
            menuInflater.inflate(R.menu.option_menu_before_login, menu)
        } else {
            menuInflater.inflate(R.menu.option_menu_after_login, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_signup -> {
                val intent = Intent(this@MainActivity, SignUpActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_login -> {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_account_info->{
                val intent = Intent(this@MainActivity, ShowAcctActivity::class.java)
                Log.d("TAG","${user.toString()}")
                intent.putExtra("extra_user",user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_edit_account_info -> {
                val intent = Intent(this, EditAcctInfoActivity::class.java)
                intent.putExtra("extra_user",user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_edit_password -> {
                val intent = Intent(this@MainActivity, EditPasswordActivity::class.java)
                intent.putExtra("extra_user",user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_logout -> {
                logout()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        perfEditor.remove("KEY_USER")
        perfEditor.apply()
        Toast.makeText(this@MainActivity, "Logout Success", Toast.LENGTH_LONG).show()
        invalidateOptionsMenu()
    }

    private fun checkLogin() {
        val gson = Gson()
        val userFromSP = sharedPreferences.getString("KEY_USER", null)
        if (userFromSP != null) {
            user = gson.fromJson(userFromSP, User::class.java)
        }
    }
}