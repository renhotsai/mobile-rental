package com.hy.group3_project.models.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.hy.group3_project.R
import com.hy.group3_project.models.enums.Roles
import com.hy.group3_project.models.properties.Property
import com.hy.group3_project.models.users.User
import java.text.NumberFormat


class PropertyAdapter(
    var propertyList: MutableList<Property>,
    var addFavHandler: (Int) -> Unit,
    var removeFavHandler: (Int) -> Unit,
    var showDetailViewHandler: (Int) -> Unit,
    var redirectLogin: () -> Unit,
    var user: User?,
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {
    val TAG = this.javaClass.simpleName
    var auth = Firebase.auth

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
            currProperty.imageName,
            "drawable",
            context.packageName
        )


        val ivProperty = holder.itemView.findViewById<ImageView>(R.id.listingImage)
        ivProperty.setImageResource(propertyImage)

        val propertyPrice = holder.itemView.findViewById<TextView>(R.id.propertyPrice)

        // Format the property price with a dollar sign and comma separator
        val formattedPrice = NumberFormat.getCurrencyInstance().format(currProperty.price)

        propertyPrice.text = formattedPrice

        val bedText = holder.itemView.findViewById<TextView>(R.id.bedAndBathAndCatAndParking)

        val concatenatedText =
            "${currProperty.beds} | ${currProperty.baths} | ${if (!currProperty.petFriendly) "Pet" else "No Pets"}"
        bedText.text = concatenatedText

        val propertyLocation = holder.itemView.findViewById<TextView>(R.id.location)

        val locationConcatenatedText =
            "${currProperty.address}, ${currProperty.city}"
        propertyLocation.text = locationConcatenatedText

        val favToggle = holder.itemView.findViewById<ToggleButton>(R.id.favToggle)
        if (propertyList[position].isFavourite) {
            favToggle.isChecked = true
        }

        if (user != null) {
            if (user!!.showList()!!.find { it == propertyList[position].id } != null) {
                favToggle.isChecked = true
            }
        }


        favToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d(TAG, auth.currentUser.toString())
                if (auth.currentUser == null) {
                    redirectLogin()
                } else {
                    addFavHandler(position)
                }
            } else {
                removeFavHandler(position)
            }
        }

        if (auth.currentUser != null) {
            Log.d(TAG, "$user")
            if (user!!.role == Roles.Landlord.toString()) {
                favToggle.isVisible = false
            }
        }
    }

    fun updateUser(newUser: User?) {
        user = newUser
        notifyDataSetChanged()
    }

    fun updateUserPropertyList(list:MutableList<Property>){
        Log.d(TAG,"list: $list")
        propertyList = list
        notifyDataSetChanged()
    }
}