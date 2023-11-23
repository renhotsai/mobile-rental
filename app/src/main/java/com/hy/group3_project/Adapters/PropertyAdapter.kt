package com.hy.group3_project.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
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
    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        // get the current property
        val currProperty: Property = propertyList.get(position)

        // Populate the views with property details
        val context = holder.itemView.context
        val propertyImage = context.resources.getIdentifier(currProperty.imageFileName, "drawable", context.packageName)


        val ivProperty = holder.itemView.findViewById<ImageView>(R.id.imageView3)
        ivProperty.setImageResource(propertyImage)

        val propertyPrice = holder.itemView.findViewById<TextView>(R.id.propertyPrice)

        // Format the property price with a dollar sign and comma separator
        val formattedPrice = NumberFormat.getCurrencyInstance().format(currProperty.propertyPrice)

        propertyPrice.text = formattedPrice

        val bedText = holder.itemView.findViewById<TextView>(R.id.bedText)
        bedText.text = currProperty.beds.toString()

        val bathText = holder.itemView.findViewById<TextView>(R.id.bathText)
        bathText.text = currProperty.baths.toString()

        val petFriendlyText = holder.itemView.findViewById<TextView>(R.id.petFriendlyText)
        val petFriendlyStatus = if (currProperty.petFriendly) "" else "Pets"
        petFriendlyText.text = petFriendlyStatus

        val addressText = holder.itemView.findViewById<TextView>(R.id.addressText)
        addressText.text = currProperty.propertyAddress.toString()
    }


}