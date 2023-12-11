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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.controllers.properties.PropertyRepository
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
import com.hy.group3_project.views.users.ShowAcctActivity
import com.hy.group3_project.views.users.SignUpActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    private var TAG = this.javaClass.simpleName
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    var user: User? = null
    var isLogin: Boolean = false
    var isLandlord: Boolean = false
    lateinit var propertyRepository: PropertyRepository
    var propertyList: MutableList<Property> = mutableListOf()

    lateinit var adapter: PropertyAdapter

    //test data
//    private fun createTestUser() {
//
//        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
//        if (userListFromSP == null) {
//            val userList = mutableListOf<User>(
//                User("Judith", "Olivia", "judith@gmail.com", Roles.Tenant.toString(), "1234"),
//                User("Michael", "Caine", "michael@gmail.com", Roles.Landlord.toString(), "1234"),
//                User("Julie", "Andrews", "julie@gmail.com", Roles.Tenant.toString(), "1234")
//            )
//            val gson = Gson()
//            val userListJson = gson.toJson(userList)
//            prefEditor.putString("KEY_USERLIST", userListJson)
//            prefEditor.apply()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Initialize shared preferences
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        this.propertyRepository = PropertyRepository(applicationContext)

        //createTestUser()
        checkLogin()
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
        invalidateOptionsMenu()
    }

    fun loadAllData() {
        propertyRepository.retrieveAllProperties()
        propertyRepository.allProperties.observe(
            this,
            androidx.lifecycle.Observer { propertiesList ->
                if (propertiesList != null) {
                    propertyList.clear()
                    propertyList.addAll(propertiesList)
                    adapter.notifyDataSetChanged()
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "onCreateOptionsMenu: ")
        Log.d(TAG, "firebase: $firebaseUser")

        if (firebaseUser != null) {
            // Retrieve user data from Firestore using the UID
            val userId = firebaseUser.uid

            // Replace "users" with the actual path to your users collection in Firestore
            val userDocument = FirebaseFirestore.getInstance().collection("users").document(userId)

            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val role = documentSnapshot.getString("role")
                    Log.d(TAG, "Role: $role")
                    // Now you can use the role value
                    when (role) {
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
            }.addOnFailureListener { exception ->
                // Handle failures
                Log.e(TAG, "Error getting document", exception)
            }
        }
//        if (isLogin) {
//            when (user!!.role) {
//                Roles.Tenant.toString() -> {
//                    menuInflater.inflate(R.menu.option_menu_tenant, menu)
//                }
//
//                Roles.Landlord.toString() -> {
//                    menuInflater.inflate(R.menu.option_menu_landlord, menu)
//                }
//            }
//        } else {
//            menuInflater.inflate(R.menu.option_menu_guest, menu)
//        }
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

    fun checkLogin() {
        val gson = Gson()
        val userFromSP = sharedPreferences.getString("KEY_USER", null)
        if (userFromSP != null) {
            this.user = gson.fromJson(userFromSP, User::class.java)
            this.isLogin = true
            this.isLandlord = user!!.role == Roles.Landlord.toString()
        }
    }

    fun getUserList(): MutableList<User> {
        var userList = mutableListOf<User>()
        val gson = Gson()
        val userListFromSP = sharedPreferences.getString("KEY_USERLIST", null)
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

    fun viewRowDetail(position: Int) {
        val selectedProperty: Property = propertyList[position]
        val propertyDetailIntent = Intent(this, PropertyDetailActivity::class.java)

        propertyDetailIntent.putExtra("PROPERTY_ID", selectedProperty.id)

        // Assuming you have a method to get the user's role, replace "getUserRole()" with the actual method call.

        // Pass some item to PropertyDetailActivity based on user role
        propertyDetailIntent.putExtra(
            "BLOCK_UPDATE_DELETE",
            "Main page block access update and delete"
        )


        startActivity(propertyDetailIntent)
    }

    fun addFav(position: Int) {
        if (isLogin && !isLandlord) {
            Log.d("UserList", "Add FavList")
            val selectedProperty: Property = propertyList[position]
            var userList = getUserList()
            var user = userList.find { it.id == user!!.id }

            user!!.addList(selectedProperty)
            updateData(user, userList)
        }
    }

    fun removeFav(position: Int) {
        if (isLogin && !isLandlord) {
            Log.d("UserList", "Remove FavList")
            var userList = getUserList()
            var user = userList.find { it.id == user!!.id }

            val propertyId = propertyList[position].id
            user!!.removeList(propertyId)

            updateData(user, userList)
        }
    }

    fun updateData(user: User, userList: MutableList<User>) {
        val gson = Gson()
        val userJson = gson.toJson(user)
        prefEditor.putString("KEY_USER", userJson)

        val userListJson = gson.toJson(userList)
        prefEditor.putString("KEY_USERLIST", userListJson)

        prefEditor.apply()
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
}