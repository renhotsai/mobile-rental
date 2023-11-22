package com.hy.group3_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hy.group3_project.databinding.ActivityMainBinding
import com.hy.group3_project.databinding.ActivityProfileViewBinding

class Profile_View : AppCompatActivity() {

    private lateinit var binding : ActivityProfileViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // this is temp need to filter it by user
        val listingForProfile : List<Listing> = mutableListOf(Listing(), Listing(), Listing())

        // setup the adapter
        val adapter = ListingViewAdaptor(listingForProfile)
        binding.rv.setAdapter(adapter)

        // configure the recycler view
        binding.rv.layoutManager = LinearLayoutManager(this)


        // add a line between each row item
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}