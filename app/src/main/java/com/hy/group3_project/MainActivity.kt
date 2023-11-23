package com.hy.group3_project

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.hy.group3_project.Enums.Roles
import com.hy.group3_project.Models.User
import com.hy.group3_project.ViewActivities.Account.AddPropertyActivity
import com.hy.group3_project.ViewActivities.Account.EditAcctInfoActivity
import com.hy.group3_project.ViewActivities.Account.EditPasswordActivity
import com.hy.group3_project.ViewActivities.Account.LoginActivity
import com.hy.group3_project.ViewActivities.Account.ShowAcctActivity
import com.hy.group3_project.ViewActivities.Account.ShowPropertyActivity
import com.hy.group3_project.ViewActivities.Account.SignUpActivity
import com.hy.group3_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        setSupportActionBar(this.binding.tbOptionMenu)
        createTestUser()
        checkLogin()

        // -----for recycle view

        val listingList: List<Listing> = mutableListOf(
            Listing("rental_1", "$2000", "Apartment", "3 Rooms | 2 Bath", "123 ABC, Toronto", false),
            Listing("rental_2", "$2200", "House", "4 Beds | 3 Baths", "789 DEF, Toronto", false),
            Listing("rental_3", "$1800", "Apartment", "2 Beds | 1.5 Baths", "456 XYZ, Toronto", true),
            Listing("rental_4", "$1500", "Basement", "1 Room | 1 Bath", "101 Elm St, Hamletville", false),
            Listing("rental_5", "$2500", "House", "5 Beds | 4 Baths", "202 Maple St, Boroughburg", true),
            Listing()
        )


        //  the adapter
        val adapter = ListingViewAdaptor(listingList, {pos -> addFav(pos) }, {pos -> removeFav(pos) }, {pos -> showDetailView(pos) })
        binding.rv.setAdapter(adapter)

        //  the recycler view
        binding.rv.layoutManager = LinearLayoutManager(this)


        // for a line between each row item
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )


        // -- filter functionality
        binding.filterBtn.setOnClickListener(){
            // for popup
            val myPopup = MyPopup(this)

            myPopup.show()
        }


    }

    private fun createTestUser() {
        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
        if (userListFromSP==null) {
            val userList = mutableListOf<User>(
                User("Judith","Olivia","judith@gmail.com",Roles.Tenants,"1234"),
                User("Michael","Caine","michael@gmail.com",Roles.Landlords,"1234"),
                User("Julie","Andrews","julie@gmail.com",Roles.Tenants,"1234")
            )
            val gson = Gson()
            val userListJson = gson.toJson(userList)
            prefEditor.putString("KEY_USERLIST",userListJson)
            prefEditor.apply()
        }
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
            R.id.menu_item_post_rental -> {
                val intent = Intent(this@MainActivity, AddPropertyActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_property_list -> {
                val intent = Intent(this@MainActivity, ShowPropertyActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_rent_property -> {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                return true
            }

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
        prefEditor.remove("KEY_USER")
        prefEditor.apply()
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

    fun addFav (rowPosition: Int){
        val snackbar = Snackbar.make(binding.rootLayout, "Added to Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun removeFav (rowPosition: Int){
        val snackbar = Snackbar.make(binding.rootLayout, "Removed from Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun showDetailView (rowPosition: Int){

//        val intent = Intent(this, TargetActivity::class.java)
//        startActivity(intent)

        val snackbar = Snackbar.make(binding.rootLayout, "Will redirect to new view", Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}