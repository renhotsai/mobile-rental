package com.hy.group3_project.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.hy.group3_project.Listing
import com.hy.group3_project.Models.Property
import com.hy.group3_project.R
import java.text.NumberFormat


class PropertyAdapter (
    private val propertyList: MutableList<Property>,
    private val rowClickHandler: (Int) -> Unit) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {
        inner class PropertyViewHolder(itemView: View): RecyclerView.ViewHolder (itemView) {
            init {
                itemView.setOnClickListener {
                    rowClickHandler(adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_item_property, parent, false)
            return PropertyViewHolder(view)
        }
    override fun getItemCount(): Int {
        return propertyList.size
    }

    fun updatePropertyDataset(newList: List<Property>?) {
        propertyList.clear()
        newList?.let {
            propertyList.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        // get the current property
        val currProperty: Property = propertyList.get(position)

        // Populate the views with property details
        val context = holder.itemView.context
        val propertyImage = context.resources.getIdentifier(currProperty.imageFileName, "drawable", context.packageName)


        val ivProperty = holder.itemView.findViewById<ImageView>(R.id.listingImage)
        ivProperty.setImageResource(propertyImage)

        val propertyPrice = holder.itemView.findViewById<TextView>(R.id.propertyPrice)

        // Format the property price with a dollar sign and comma separator
        val formattedPrice = NumberFormat.getCurrencyInstance().format(currProperty.propertyPrice)

        propertyPrice.text = formattedPrice

        val bedText = holder.itemView.findViewById<TextView>(R.id.bedAndBathAndCatAndParking)

        val concatenatedText = "${currProperty.beds} | ${currProperty.baths} | ${if (!currProperty.petFriendly) "Pet" else "No Pets"}"
        bedText.text = concatenatedText

        val propertyLocation = holder.itemView.findViewById<TextView>(R.id.location)

        val locationConcatenatedText = "${currProperty.propertyAddress}, ${currProperty.propertyCity}"
        propertyLocation.text = locationConcatenatedText
    }


}