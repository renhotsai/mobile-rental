package com.hy.group3_project

import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.gson.Gson
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.controllers.users.UserRepository
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.enums.Roles
import com.hy.group3_project.models.properties.Property
import com.hy.group3_project.models.users.User
import com.hy.group3_project.views.properties.AddPropertyActivity
import com.hy.group3_project.views.properties.PropertyDetailActivity
import com.hy.group3_project.views.properties.ShowPropertyActivity
import com.hy.group3_project.views.users.EditAcctInfoActivity
import com.hy.group3_project.views.users.EditPasswordActivity
import com.hy.group3_project.views.users.FavoriteActivity
import com.hy.group3_project.views.users.LoginActivity
import com.hy.group3_project.views.users.SignUpActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    var TAG = this.javaClass.simpleName
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    var user: User? = null
    lateinit var propertyRepository: PropertyRepository
    lateinit var userRepository: UserRepository
    lateinit var auth: FirebaseAuth
    var propertyList: MutableList<Property> = mutableListOf()

    lateinit var adapter: PropertyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Initialize shared preferences
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        this.propertyRepository = PropertyRepository(applicationContext)
        this.userRepository = UserRepository(applicationContext)

        auth = Firebase.auth
        checkLogin()

    }

    override fun onResume() {
        super.onResume()
        checkLogin()
        invalidateOptionsMenu()
    }



    fun loadUserData() {
        userRepository.getUserFromDB(user!!.id)
        userRepository.userFromDB.observe(this){userFromDB->
            user = userFromDB
            prefEditorUser(user!!)
        }
        Log.d(TAG,"loadUserData: $user")
        propertyRepository.getPropertiesWithId(user!!.showList())
        propertyRepository.userProperties.observe(this){ propertiesList ->
            propertyList.clear()
            propertyList.addAll(propertiesList)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        checkLogin()
        if (user != null) {
            when (user!!.role) {
                Roles.Tenant.toString() -> {
                    menuInflater.inflate(R.menu.option_menu_tenant, menu)
                }

                Roles.Landlord.toString() -> {
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
            R.id.menu_item_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_item_favorite_list -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
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

    fun logout() {
        auth.signOut()
        prefEditor.clear().apply()
        Toast.makeText(this, "Logout Success", Toast.LENGTH_LONG).show()
        redirectMain()
    }

    fun checkLogin() {
        val gson = Gson()
        val userFromSP = sharedPreferences.getString("KEY_USER", null)
        if (userFromSP != null) {
            user = gson.fromJson(userFromSP, User::class.java)
        }
    }

    fun redirectMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun viewRowDetail(position: Int) {
        val selectedProperty: Property = propertyList[position]
        val propertyDetailIntent = Intent(this, PropertyDetailActivity::class.java)

        propertyDetailIntent.putExtra("PROPERTY_ID", selectedProperty.id)

        // Assuming you have a method to get the user's role, replace "getUserRole()" with the actual method call.
        startActivity(propertyDetailIntent)
    }

    fun addToUserList(position: Int) {
        if (auth.currentUser != null && user!!.role != Roles.Landlord.toString()) {
            Log.d("UserList", "Add FavList")
            val propertyId = propertyList[position].id
            user!!.addList(propertyId)
            userRepository.updateUser(user!!)

            val gson = Gson()
            val userJson = gson.toJson(user)
            prefEditor.putString("KEY_USER", userJson)

            prefEditor.apply()
        }
    }

    fun removeFromUserList(position: Int) {
        if (auth.currentUser != null && user!!.role != Roles.Landlord.toString()) {
            Log.d(TAG, "Remove FavList")

            val propertyId = propertyList[position].id
            user!!.removeList(propertyId)
            userRepository.updateUser(user!!)
            loadUserData()
        }
    }

    fun updateData(user: User, userList: MutableList<User>) {

    }

    fun getAddress(address: String): Address? {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val searchResults = geocoder.getFromLocationName(address, 1)
            if (searchResults == null) {
                Log.d(TAG, "searchResults is null")
                return null
            }
            if (searchResults.size > 0) {
                val foundLocation = searchResults[0]
                Log.d(TAG, "Coordinates are: ${foundLocation.latitude}, ${foundLocation.longitude}")
                return foundLocation
            }
            return null
        } catch (ex: Exception) {
            Log.e(TAG, ex.toString())
            return null
        }
    }

    fun prefEditorUser(user: User) {
        val gson = Gson()
        val userJson = gson.toJson(user)
        prefEditor.putString("KEY_USER", userJson)
        prefEditor.apply()
    }

    fun afterLoginAndSignup() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}