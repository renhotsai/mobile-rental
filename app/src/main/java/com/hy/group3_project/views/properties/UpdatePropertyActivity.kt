package com.hy.group3_project.views.properties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.databinding.ActivityUpdatePropertyBinding
import com.hy.group3_project.models.properties.Property
import kotlinx.coroutines.launch

class UpdatePropertyActivity : BaseActivity() {
    lateinit var binding: ActivityUpdatePropertyBinding
    private lateinit var selectedProperty: Property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityUpdatePropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbOptionMenu)

        val propertyId = intent.getStringExtra("PROPERTY_ID")!!

        lifecycleScope.launch {
            val propertyFromDb = propertyRepository.findProperty(propertyId)

            Log.d(TAG, propertyFromDb.toString())
            if (propertyFromDb != null) {

                selectedProperty = propertyFromDb
                // Assuming you have a List of property types
                setPropertyDetail(selectedProperty)
            }


            binding.btnReset.setOnClickListener {
                setPropertyDetail(propertyFromDb!!)
            }

            binding.btnUpdate.setOnClickListener {
                saveData()
            }
        }
    }

    private fun setPropertyDetail(selectedProperty: Property) {
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
        binding.editContactInfo.hint = selectedProperty.contactInfo
    }

    private fun saveData() {
        selectedProperty.type = binding.spinnerPropertyType.selectedItem.toString()
        selectedProperty.beds = binding.spinnerBeds.selectedItem.toString()
        selectedProperty.baths = binding.spinnerBaths.selectedItem.toString()
        selectedProperty.petFriendly = binding.spinnerPet.selectedItem.toString().toBoolean()
        selectedProperty.canParking = binding.spinnerParking.selectedItem.toString().toBoolean()
        selectedProperty.availability = binding.swRentAv.isChecked

        if (!binding.editPropertyPrice.text.toString().isNullOrEmpty()) {
            selectedProperty.price = binding.editPropertyPrice.text.toString().toInt()
        }

        if (!binding.editPropertyDesc.text.toString().isNullOrEmpty()) {
            selectedProperty.desc = binding.editPropertyDesc.text.toString()
        }

        if (!binding.editPropertyCity.text.toString().isNullOrEmpty()) {
            selectedProperty.city = binding.editPropertyCity.text.toString()
        }

        if (!binding.editPropertyAddress.text.toString().isNullOrEmpty()) {
            selectedProperty.address = binding.editPropertyAddress.text.toString()
        }

        if (!binding.editContactInfo.text.toString().isNullOrEmpty()) {
            selectedProperty.contactInfo = binding.editContactInfo.text.toString()
        }


        Log.d(TAG, "check update property: $selectedProperty")
        propertyRepository.addPropertyToDB(selectedProperty)


        val showPropertyIntent =
            Intent(this@UpdatePropertyActivity, ShowPropertyActivity::class.java)
        startActivity(showPropertyIntent)
    }
}
