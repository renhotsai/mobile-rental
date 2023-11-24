package com.hy.group3_project.ViewActivities.Account


import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Adapters.PropertyAdapter
import com.hy.group3_project.Models.Property
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityShowPropertyBinding

class ShowPropertyActivity : BaseActivity() {

    lateinit var binding: ActivityShowPropertyBinding
    lateinit var adapter: PropertyAdapter

    // Mutable list to store properties
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        // Setup adapter
        adapter = PropertyAdapter(
            user.showList(),
            {pos-> addFav(pos) },
            {pos-> removeFav(pos)},
            {pos->viewRowDetail(pos)},
            isLandlord,
            isLogin,
            { redirectLogin() }
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

        propertyDataSource.clear()
        propertyDataSource.addAll(user.showList())
        adapter.notifyDataSetChanged()

    }
}
