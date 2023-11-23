package com.hy.group3_project

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListingViewAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        // ----- data for recycle view

        val listingList: List<Listing> = mutableListOf(
            Listing("rental_1", 2000, "Apartment", 3 , 2 , "123 ABC, Toronto", false),
            Listing("rental_2", 2200, "House", 4 , 2 ,"789 DEF, Toronto", false),
            Listing("rental_3", 1800, "Apartment", 2 ,2, "456 XYZ, Toronto", true),
            Listing("rental_4", 1500, "Basement", 1 , 1 , "101 Elm St, Hamletville", false),
            Listing("rental_5", 2500, "House", 5 ,4 , "202 Maple St, Boroughburg", true),
            Listing()
        )

        var listForAdaptor = listingList

        // -- filter functionality
        binding.filterBtn.setOnClickListener(){
            // for popup
            val myPopup = MyPopup(this)

            myPopup.show()
        }

        // -- Search functionality

        binding.searchButton.setOnClickListener(){
            val searchText: String? = binding.searchText.text?.toString()

            val filteredList = listingList.filter { listing ->
                listing.location?.contains(searchText ?: "", ignoreCase = true) == true
            }

            listForAdaptor = filteredList
            adapter.notifyDataSetChanged()

        }


        // --- the adapter for rv
        adapter = ListingViewAdaptor(listForAdaptor, {pos -> addFav(pos) }, {pos -> removeFav(pos) }, {pos -> showDetailView(pos) })
        binding.rv.setAdapter(adapter)

        //  the recycler view
        binding.rv.layoutManager = LinearLayoutManager(this)


        // for a line between each row item
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

    fun addFav (rowPosition: Int){
        val snackbar = Snackbar.make(binding.rootLayout, "Added to Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun removeFav (rowPosition: Int){
        val snackbar = Snackbar.make(binding.rootLayout, "Removed from Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun showDetailView (rowPosition: Int){

//        val intent = Intent(this, TargetActivity::class.java)
//        startActivity(intent)

        val snackbar = Snackbar.make(binding.rootLayout, "Will redirect to new view", Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}