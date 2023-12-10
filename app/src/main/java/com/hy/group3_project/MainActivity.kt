package com.hy.group3_project

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.databinding.ActivityMainBinding
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.properties.Property
import com.hy.group3_project.views.MyPopup

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PropertyAdapter


    private var displayedPropertyList: MutableList<Property> = mutableListOf()
    private var displayedProperties: List<Property> = emptyList()
    private var favoriteList: MutableList<Property> = mutableListOf()

    private lateinit var propertyRepository:PropertyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        this.propertyRepository = PropertyRepository(applicationContext)

        if (user != null) {
            favoriteList = user!!.showList()
        }

        // Setup adapter
        adapter = PropertyAdapter(
            displayedPropertyList,
            { pos -> addFav(pos) },
            { pos -> removeFav(pos) },
            { pos -> viewRowDetail(pos) },
            isLandlord,
            isLogin,
            { redirectLogin() },
            favoriteList
        )

        // ----- data for recycle view
        binding.rvProperties.adapter = adapter
        binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        // -- filter functionality
        val myPopup = MyPopup(this)
        binding.filterBtn.setOnClickListener() {
            MyPopup(this)
            myPopup.show()
        }

        // -- Search functionality
        binding.searchButton.setOnClickListener() {
            val searchText: String? = binding.searchText.text?.toString()

            displayedProperties = displayedPropertyList.filter { property ->
                property.address?.contains(searchText ?: "", ignoreCase = true) == true
            }

            adapter.updatePropertyDataset(displayedProperties,favoriteList)
        }
    }

    override fun onResume() {
        super.onResume()

        propertyRepository.retrieveAllProperties()
        propertyRepository.allProperties.observe(this, androidx.lifecycle.Observer {
            propertiesList ->
            if(propertiesList !=null){
                displayedPropertyList.clear()
                displayedPropertyList.addAll(propertiesList)
                adapter.notifyDataSetChanged()
            }
        })
    }
}