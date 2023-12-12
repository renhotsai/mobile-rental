package com.hy.group3_project.views.properties


import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityShowPropertyBinding
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.properties.Property

class ShowPropertyActivity : BaseActivity() {


    lateinit var binding: ActivityShowPropertyBinding

    // Mutable list to store properties
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        Log.d(TAG, user.toString())

        val userPropertyList = user!!.showList()
        var mutableListOf: MutableList<Property> = mutableListOf()
        propertyRepository.getPropertiesWithId(userPropertyList)
        // Setup adapter
        adapter = PropertyAdapter(
            propertyList,
            { pos -> addToUserList(pos) },
            { pos -> removeFromUserList(pos) },
            { pos -> viewRowDetail(pos) },
            { redirectLogin() },
            user
        )

        // Setup RecyclerView
        binding.rvProperties.adapter = adapter
        binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )


    }


    // Helper function to retrieve properties from SharedPreferences
    override fun onResume() {
        super.onResume()
        adapter.updateUser(user)
        loadUserData()
    }
}
