package com.hy.group3_project.ViewActivities.Account

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Models.Property
import com.hy.group3_project.R
import com.hy.group3_project.databinding.ActivityUpdatePropertyBinding

class UpdatePropertyActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdatePropertyBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityUpdatePropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        val propertyId = intent.getStringExtra("PROPERTY_ID")

        val propertyListFromSP = sharedPreferences.getString("KEY_PROPERTY_DATASOURCE", "")

        val gson = Gson()
        val typeToken = object: TypeToken<List<Property>>() {}.type
        val propertyList = gson.fromJson<List<Property>>(propertyListFromSP, typeToken).toMutableList()

        val selectedProperty = propertyList.find { it.id == propertyId }

        if (selectedProperty != null) {
            if (selectedProperty != null) {
                // Assuming you have a List of property types
                val propertyTypesList = listOf("Condo", "House", "Basement", "Apartment")
                val propertyBedList = listOf("Studio", "1 bed", "2 beds", "3 beds", "3 beds+")
                val propertyBathList = listOf("0 bath", "1 bath", "2 baths", "2 baths+")
                val propertyPet = listOf("Yes", "No")
                val propertyParking = listOf("Yes", "No")

                // Get the index of the property type in the list
                val indexOfPropertyType = propertyTypesList.indexOf(selectedProperty.propertyType)
                val indexOfPropertyBed = propertyBedList.indexOf(selectedProperty.beds)
                val indexOfPropertyBath = propertyBathList.indexOf(selectedProperty.baths)
                val indexOfPropertyPet = propertyPet.indexOf(selectedProperty.petFriendly.toString())
                val indexOfPropertyParking = propertyParking.indexOf(selectedProperty.propertyParking.toString())

                // Set the selection in the spinner
                binding.spinnerPropertyType.setSelection(indexOfPropertyType)
                binding.spinnerBeds.setSelection(indexOfPropertyBed)
                binding.spinnerBaths.setSelection(indexOfPropertyBath)
                binding.spinnerPet.setSelection(indexOfPropertyPet)
                binding.spinnerParking.setSelection(indexOfPropertyParking)
            }


            binding.editPropertyCity.text = Editable.Factory.getInstance().newEditable(selectedProperty.propertyCity)
            binding.editPropertyPrice.text = Editable.Factory.getInstance().newEditable(selectedProperty.propertyPrice.toString())
            binding.editPropertyDesc.text = Editable.Factory.getInstance().newEditable(selectedProperty.propertyDesc)
            binding.editPropertyAddress.text = Editable.Factory.getInstance().newEditable(selectedProperty.propertyAddress)
            binding.swRentAv.isChecked = selectedProperty.rentAvailability
        }

        binding.btnUpdate.setOnClickListener {
            // Get the updated values from UI elements
            val updatedPropertyType = binding.spinnerPropertyType.selectedItem.toString()
            val updatedPropertyBeds = binding.spinnerBeds.selectedItem.toString()
            val updatedPropertyBaths = binding.spinnerBaths.selectedItem.toString()
            val updatedPropertyPet = binding.spinnerPet.selectedItem.toString().toBoolean()
            val updatedPropertyParking = binding.spinnerParking.selectedItem.toString().toBoolean()
            val updatedPropertyPrice = binding.editPropertyPrice.text.toString().toInt()
            val updatedPropertyDesc = binding.editPropertyDesc.text.toString()
            val updatedPropertyCity = binding.editPropertyCity.text.toString()
            val updatedPropertyAddress = binding.editPropertyAddress.text.toString()
            val updatedPropertyAvailability = binding.swRentAv.isChecked
            // Update the selectedProperty with the new values
            selectedProperty?.apply {
                Log.d("UpdatePropertyActivity", "Before Update: $this")

                propertyType = updatedPropertyType
                beds = updatedPropertyBeds
                baths = updatedPropertyBaths
                petFriendly = updatedPropertyPet
                propertyParking = updatedPropertyParking
                propertyPrice = updatedPropertyPrice
                propertyDesc = updatedPropertyDesc
                propertyCity = updatedPropertyCity
                propertyAddress = updatedPropertyAddress
                rentAvailability = updatedPropertyAvailability
            }

            val index = propertyList.indexOfFirst { it.id == selectedProperty?.id }
            if (index != -1) {
                propertyList[index] = selectedProperty!!
            }

            // Save the updated property list to SharedPreferences
            val jsonPropertyList = gson.toJson(propertyList)
            prefEditor.putString("KEY_PROPERTY_DATASOURCE", jsonPropertyList)
            prefEditor.apply()

            val propertyUpdateIntent = Intent(this@UpdatePropertyActivity, PropertyDetailActivity::class.java)

            propertyUpdateIntent.putExtra("PROPERTY_ID", selectedProperty?.id)

            startActivity(propertyUpdateIntent)

        }



    }
}