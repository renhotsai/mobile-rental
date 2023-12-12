package com.hy.group3_project.views.users

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityFavoriteBinding
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.properties.Property

class FavoriteActivity : BaseActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    private var displayedProperties: List<Property> = emptyList()
    private var favoriteList: MutableList<Property> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)


            propertyRepository.getPropertiesWithId(user!!.showList())
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

    }

    override fun onResume() {
        super.onResume()
        adapter.updateUser(user)
        loadUserData()
    }
}