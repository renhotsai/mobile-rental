package com.hy.group3_project

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ListingViewAdaptor(var listingData: List<Listing>,
    private val addFavHandler: (Int) -> Unit, private val removeFavHandler: (Int) -> Unit, private val showDetailViewHandler: (Int) -> Unit
) : RecyclerView.Adapter<ListingViewAdaptor.ListingViewHolder>(){

    // for search and filter
    fun updateDataset(newList: List<Listing>) {
        listingData = newList
        notifyDataSetChanged()
    }
    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener{
                showDetailViewHandler(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_view, parent, false)
        return ListingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listingData.size
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {

        // get all rv views
        val price = holder.itemView.findViewById<TextView>(R.id.price)
        val propertyType = holder.itemView.findViewById<TextView>(R.id.type)
        val rooms = holder.itemView.findViewById<TextView>(R.id.rooms)
        val location = holder.itemView.findViewById<TextView>(R.id.location)
        val image = holder.itemView.findViewById<ImageView>(R.id.listingImage)
        val favToggle = holder.itemView.findViewById<ToggleButton>(R.id.favToggle)

        // set data to rv
        price.text = "$ ${listingData[position].price}"
        propertyType.text = listingData[position].type
        rooms.text = "${listingData[position].rooms} Rooms | ${listingData[position].bath} Bath"
        location.text = listingData[position].location

        // for fav to stop functino running at the start
        favToggle.setOnCheckedChangeListener(null)
        favToggle.isChecked = listingData[position].isFavorite

        // for fav functionality
        favToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addFavHandler(position)
            } else {
                removeFavHandler(position)

            }
        }


        // for image
        val imageResource = holder.itemView.context.resources.getIdentifier(listingData[position].img, "drawable", holder.itemView.context.packageName)
        image.setImageResource(imageResource)



    }
}