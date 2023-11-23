package com.hy.group3_project.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hy.group3_project.Enums.Roles
import com.hy.group3_project.Listing
import com.hy.group3_project.Models.Property
import com.hy.group3_project.Models.User
import com.hy.group3_project.R
import java.text.NumberFormat


class PropertyAdapter(
    private var propertyList: MutableList<Property>,
    var addFavHandler: (Int) -> Unit,
    var removeFavHandler: (Int) -> Unit,
    var showDetailViewHandler: (Int) -> Unit,
    var isLandlord:Boolean
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {
    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                showDetailViewHandler(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }


    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        // get the current property
        val currProperty: Property = propertyList.get(position)

        // Populate the views with property details
        val context = holder.itemView.context
        val propertyImage = context.resources.getIdentifier(
            currProperty.imageFileName,
            "drawable",
            context.packageName
        )


        val ivProperty = holder.itemView.findViewById<ImageView>(R.id.listingImage)
        ivProperty.setImageResource(propertyImage)

        val propertyPrice = holder.itemView.findViewById<TextView>(R.id.propertyPrice)

        // Format the property price with a dollar sign and comma separator
        val formattedPrice = NumberFormat.getCurrencyInstance().format(currProperty.propertyPrice)

        propertyPrice.text = formattedPrice

        val bedText = holder.itemView.findViewById<TextView>(R.id.bedAndBathAndCatAndParking)

        val concatenatedText =
            "${currProperty.beds} | ${currProperty.baths} | ${if (!currProperty.petFriendly) "Pet" else "No Pets"}"
        bedText.text = concatenatedText

        val propertyLocation = holder.itemView.findViewById<TextView>(R.id.location)

        val locationConcatenatedText =
            "${currProperty.propertyAddress}, ${currProperty.propertyCity}"
        propertyLocation.text = locationConcatenatedText

        val favToggle = holder.itemView.findViewById<ToggleButton>(R.id.favToggle)
        if (isLandlord) {
            favToggle.isVisible = false
        }else{
            favToggle.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    addFavHandler(position)
                } else {
                    removeFavHandler(position)
                }
            }
        }
    }

    fun updatePropertyDataset(newList: List<Property>?) {
        propertyList.clear()
        newList?.let {
            propertyList.addAll(it)
        }
        notifyDataSetChanged()
    }
}