package com.hy.group3_project

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.databinding.ActivityMainBinding
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.properties.FilterData
import com.hy.group3_project.views.FilterApplyListener
import com.hy.group3_project.views.MyPopup

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.tbOptionMenu
        setSupportActionBar(toolbar)
        // set option menu
        drawerLayout = binding.rootLayout
        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new )
//        }
        this.propertyRepository = PropertyRepository(applicationContext)

        // Setup adapter

        adapter = PropertyAdapter(
            propertyList,
            { pos -> addToUserList(pos) },
            { pos -> removeFromUserList(pos) },
            { pos -> viewRowDetail(pos) },
            { redirectLogin() },
            user
        )

        // ----- data for recycle view
//        binding.rvProperties.adapter = adapter
//        binding.rvProperties.layoutManager = LinearLayoutManager(this)
//        binding.rvProperties.addItemDecoration(
//            DividerItemDecoration(
//                this,
//                LinearLayoutManager.VERTICAL
//            )
//        )
//
        // -- filter functionality
        binding.filterBtn.setOnClickListener {
            val searchField = binding.searchText
            val myPopup = MyPopup(this, searchField)

            // listener to handle filter events
            myPopup.filterApplyListener = object : FilterApplyListener {
                override fun onFilterApplied(filterData: FilterData) {
                    val filterList = propertyRepository.filterProperties(filterData)
                    Log.d("Filter", "$filterList")
                }
            }

            myPopup.show()
        }


        // -- Search functionality
//        binding.searchButton.setOnClickListener() {
//            val searchText: String? = binding.searchText.text?.toString()
//
//            displayedProperties = displayedPropertyList.filter { property ->
//                property.address?.contains(searchText ?: "", ignoreCase = true) == true
//            }
//
//            adapter.updatePropertyDataset(displayedProperties,favoriteList)
//        }
    }

    override fun onResume() {
        super.onResume()
        adapter.updateUser(user)
        loadAllData()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}