package com.hy.group3_project

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        // -----for recycle view

        val listingList: List<Listing> = mutableListOf(
            Listing("rental_1", "$2000", "Apartment", "3 Rooms | 2 Bath", "123 ABC, Toronto", false),
            Listing("rental_2", "$2200", "House", "4 Beds | 3 Baths", "789 DEF, Toronto", false),
            Listing("rental_3", "$1800", "Apartment", "2 Beds | 1.5 Baths", "456 XYZ, Toronto", true),
            Listing("rental_4", "$1500", "Basement", "1 Room | 1 Bath", "101 Elm St, Hamletville", false),
            Listing("rental_5", "$2500", "House", "5 Beds | 4 Baths", "202 Maple St, Boroughburg", true),
            Listing()
        )


        // setup the adapter
        val adapter = ListingViewAdaptor(listingList)
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

        // -- filter functionality
        binding.filterBtn.setOnClickListener(){
            // for popup
            val myPopup = MyPopup(this)

            myPopup.show()
        }
    }
}