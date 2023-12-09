package com.hy.group3_project.views.users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hy.group3_project.databinding.ActivityProfileViewBinding
import com.hy.group3_project.models.adapters.ListingViewAdaptor
import com.hy.group3_project.models.properties.Listing

class Profile_View : AppCompatActivity() {

    private lateinit var binding : ActivityProfileViewBinding
    private lateinit var adapter: ListingViewAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // this is temp need to filter it by user
        val listingForProfile : List<Listing> = mutableListOf(Listing(), Listing(), Listing())

        //  the adapter
        adapter = ListingViewAdaptor(listingForProfile, {pos -> addFav(pos) }, {pos -> removeFav(pos) }, {pos -> showDetailView(pos) })
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
    }

    fun addFav (rowPosition: Int){
        val snackbar = Snackbar.make(binding.rootLayout, "Added to Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun removeFav (rowPosition: Int){

        val snackbar = Snackbar.make(binding.rootLayout, "Removed from Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
        adapter.notifyDataSetChanged()
    }

    fun showDetailView (rowPosition: Int){

//        val intent = Intent(this, TargetActivity::class.java)
//        startActivity(intent)

        val snackbar = Snackbar.make(binding.rootLayout, "Will redirect to new view", Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}