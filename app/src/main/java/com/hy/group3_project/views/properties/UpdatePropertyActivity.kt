package com.hy.group3_project.views.properties

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityUpdatePropertyBinding
import kotlinx.coroutines.launch

class UpdatePropertyActivity : BaseActivity() {
    lateinit var binding: ActivityUpdatePropertyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityUpdatePropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val propertyId = intent.getStringExtra("PROPERTY_ID")!!

        lifecycleScope.launch {
            val selectedProperty = propertyRepository.findProperty(propertyId)

            Log.d(TAG, selectedProperty.toString())
            if (selectedProperty != null) {
                // Assuming you have a List of property types
                val propertyTypesList = listOf("Condo", "House", "Basement", "Apartment")
                val propertyBedList = listOf("Studio", "1 bed", "2 beds", "3 beds", "3 beds+")
                val propertyBathList = listOf("0 bath", "1 bath", "2 baths", "2 baths+")
                val propertyPet = listOf("Yes", "No")
                val propertyParking = listOf("Yes", "No")

                // Get the index of the property type in the list
                val indexOfPropertyType = propertyTypesList.indexOf(selectedProperty.type)
                val indexOfPropertyBed = propertyBedList.indexOf(selectedProperty.beds)
                val indexOfPropertyBath = propertyBathList.indexOf(selectedProperty.baths)
                val indexOfPropertyPet =
                    propertyPet.indexOf(selectedProperty.petFriendly.toString())
                val indexOfPropertyParking =
                    propertyParking.indexOf(selectedProperty.canParking.toString())

                // Set the selection in the spinner
                binding.spinnerPropertyType.setSelection(indexOfPropertyType)
                binding.spinnerBeds.setSelection(indexOfPropertyBed)
                binding.spinnerBaths.setSelection(indexOfPropertyBath)
                binding.spinnerPet.setSelection(indexOfPropertyPet)
                binding.spinnerParking.setSelection(indexOfPropertyParking)
                binding.editPropertyCity.hint = selectedProperty.city
                binding.editPropertyPrice.hint = selectedProperty.price.toString()
                binding.editPropertyDesc.hint = selectedProperty.desc
                binding.editPropertyAddress.hint = selectedProperty.address
                binding.swRentAv.isChecked = selectedProperty.availability
            }
        }
    }
}
