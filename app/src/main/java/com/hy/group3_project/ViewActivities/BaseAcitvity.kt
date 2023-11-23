package com.hy.group3_project.ViewActivities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Enums.Roles
import com.hy.group3_project.MainActivity
import com.hy.group3_project.Models.User
import com.hy.group3_project.R
import com.hy.group3_project.ViewActivities.Account.AddPropertyActivity
import com.hy.group3_project.ViewActivities.Account.EditAcctInfoActivity
import com.hy.group3_project.ViewActivities.Account.EditPasswordActivity
import com.hy.group3_project.ViewActivities.Account.LoginActivity
import com.hy.group3_project.ViewActivities.Account.ShowAcctActivity
import com.hy.group3_project.ViewActivities.Account.ShowPropertyActivity
import com.hy.group3_project.ViewActivities.Account.SignUpActivity

open class BaseActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var user: User
    var isLogin: Boolean = false

    //test data
    private fun createTestUser() {

        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
        if (userListFromSP == null) {
            val userList = mutableListOf<User>(
                User("Judith", "Olivia", "judith@gmail.com", Roles.Tenant, "1234"),
                User("Michael", "Caine", "michael@gmail.com", Roles.Landlord, "1234"),
                User("Julie", "Andrews", "julie@gmail.com", Roles.Tenant, "1234")
            )
            val gson = Gson()
            val userListJson = gson.toJson(userList)
            prefEditor.putString("KEY_USERLIST", userListJson)
            prefEditor.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Initialize shared preferences
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        createTestUser()
        checkLogin()
    }

    override fun onResume() {
        checkLogin()
        invalidateOptionsMenu()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isLogin) {
            when (user.role) {
                Roles.Tenant -> {
                    menuInflater.inflate(R.menu.option_menu_tenant, menu)
                }
                Roles.Landlord -> {
                    menuInflater.inflate(R.menu.option_menu_landlord, menu)
                }
            }
        } else {
            menuInflater.inflate(R.menu.option_menu_guest, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_favorite_list -> {
                return true
            }

            R.id.menu_item_post_rental -> {
                val intent = Intent(this, AddPropertyActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_property_list -> {
                val intent = Intent(this, ShowPropertyActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_rent_property -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_signup -> {
                redirectSignUp()
                return true
            }

            R.id.menu_item_login -> {
                redirectLogin()
                return true
            }

            R.id.menu_item_account_info -> {
                val intent = Intent(this, ShowAcctActivity::class.java)
                intent.putExtra("extra_user", user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_edit_account_info -> {
                val intent = Intent(this, EditAcctInfoActivity::class.java)
                intent.putExtra("extra_user", user)
                startActivity(intent)
                return true
            }

            R.id.menu_item_edit_password -> {
                val intent = Intent(this, EditPasswordActivity::class.java)
                intent.putExtra("extra_user", user)
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

    fun redirectSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun redirectLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        prefEditor.remove("KEY_USER")
        prefEditor.apply()
        this.isLogin = false
        Toast.makeText(this, "Logout Success", Toast.LENGTH_LONG).show()
        redirectMain()
    }

    fun getUserRole(): Roles {
        return this.user.role;
    }

    fun checkLogin() {
        val gson = Gson()
        val userFromSP = sharedPreferences.getString("KEY_USER", null)
        if (userFromSP != null) {
            this.user = gson.fromJson(userFromSP, User::class.java)
            this.isLogin = true
        }
    }

    fun getUserList(): MutableList<User> {
        var userList = mutableListOf<User>()
        val gson = Gson()
        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
        Log.d("TAG","$userListFromSP")
        if (userListFromSP != null) {
            val typeToken = object : TypeToken<MutableList<User>>() {}.type
            userList = gson.fromJson(userListFromSP, typeToken)
        }
        return userList
    }
    fun redirectMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}