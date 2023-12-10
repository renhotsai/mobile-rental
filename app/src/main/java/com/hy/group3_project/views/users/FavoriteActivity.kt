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

        // Setup adapter


        adapter = PropertyAdapter(
            user!!.showList(),
            { pos -> addFav(pos) },
            { pos -> removeFav(pos) },
            { pos -> viewRowDetail(pos) },
            isLandlord,
            isLogin,
            { redirectLogin() },
            user!!.showList()
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
        propertyList.clear()
        propertyList.addAll(user!!.showList())
        adapter.notifyDataSetChanged()
    }
}