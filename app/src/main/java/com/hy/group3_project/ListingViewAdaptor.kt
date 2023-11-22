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

class ListingViewAdaptor(var listingData: List<Listing>) : RecyclerView.Adapter<ListingViewAdaptor.ListingViewHolder>(){
    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

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
        val fav = holder.itemView.findViewById<ToggleButton>(R.id.favToggle)

        // set data to rv
        price.text = listingData[position].price
        propertyType.text = listingData[position].type
        rooms.text = listingData[position].rooms
        location.text = listingData[position].location
        fav.isChecked = listingData[position].isFavorite


        // for image
        val imageResource = holder.itemView.context.resources.getIdentifier(listingData[position].img, "drawable", holder.itemView.context.packageName)
        image.setImageResource(imageResource)



    }
}