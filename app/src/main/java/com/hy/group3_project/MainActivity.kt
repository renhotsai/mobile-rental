package com.hy.group3_project

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.databinding.ActivityMainBinding
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.properties.FilterData
import com.hy.group3_project.views.FilterApplyListener
import com.hy.group3_project.views.MyPopup

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(this.binding.tbOptionMenu)


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
        binding.rvProperties.adapter = adapter
        binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        // -- filter functionality
        binding.filterBtn.setOnClickListener {
            val searchField = binding.searchText
            val myPopup = MyPopup(this, searchField)

            // listener to handle filter events
            myPopup.filterApplyListener = object : FilterApplyListener {
                override fun onFilterApplied(filterData: FilterData) {
                    val filteredPropertiesList = propertyRepository.filterProperties(filterData)
                    // update RV
                    Log.d("Filter", "$filteredPropertiesList")
                    adapter.updateUserPropertyList(filteredPropertiesList)

                }
            }

            myPopup.show()
        }


        // -- Search functionality
        binding.searchButton.setOnClickListener {
            val searchText: String = binding.searchText.text.toString()
            val searchedPropertiesList = propertyRepository.searchPropertiesByAddress(searchText)
            // Update RV
            Log.d("Filter Search", "$searchedPropertiesList")

            adapter.updateUserPropertyList(searchedPropertiesList)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.updateUser(user)
        loadAllData()
    }


}