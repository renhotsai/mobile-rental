package com.hy.group3_project.ViewActivities.Account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Models.Property
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityAddPropertyBinding
import kotlin.random.Random

class AddPropertyActivity : BaseActivity() {

    private lateinit var binding: ActivityAddPropertyBinding
    private var savedProperties: MutableList<Property> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbOptionMenu)
        binding.btnCreate.setBackgroundColor(Color.parseColor("#05a6fc"))

        binding.btnCreate.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val propertyType = binding.spinnerPropertyType.selectedItem.toString()
        val propertyBeds = binding.spinnerBeds.selectedItem.toString()
        val propertyBaths = binding.spinnerBaths.selectedItem.toString()
        val propertyPet = binding.spinnerPet.selectedItem.toString().toBoolean()
        val propertyParking = binding.spinnerParking.selectedItem.toString().toBoolean()
        val propertyPrice = binding.editPropertyPrice.text.toString().toInt()
        val propertyDesc = binding.editPropertyDesc.text.toString()
        val propertyCity = binding.editPropertyCity.text.toString()
        val propertyAddress = binding.editPropertyAddress.text.toString()
        val propertyAvailability = binding.swRentAv.isChecked

        if (propertyBeds != "" && propertyPrice != 0 &&
            propertyBaths != "" && propertyDesc != "" && propertyAddress != "") {

            // Generate random number for the property
            val randomNumber = Random.nextInt(1, 6)

            // Create a Property object
            val propertyToAdd = Property(
                "rental_$randomNumber",
                propertyType,
                propertyBeds,
                propertyBaths,
                propertyPet,
                propertyParking,
                propertyPrice,
                propertyDesc,
                propertyCity,
                propertyAddress,
                propertyAvailability
            )

            // Retrieve existing list of properties from SharedPreferences
            val propertyListFromSP = sharedPreferences.getString("KEY_PROPERTY_DATASOURCE", "")
            val gson = Gson()

            if (propertyListFromSP != "") {
                // Convert the JSON string to a list of properties
                val typeToken = object : TypeToken<List<Property>>() {}.type
                savedProperties = gson.fromJson<List<Property>>(propertyListFromSP, typeToken).toMutableList()
            }

            // Add the new property to the existing list
            savedProperties.add(propertyToAdd)

            // Convert the updated list to a JSON string
            val updatedListAsString = gson.toJson(savedProperties)

            // Save the updated list back to SharedPreferences
            prefEditor.putString("KEY_PROPERTY_DATASOURCE", updatedListAsString)
            prefEditor.apply()

            // Start the ShowPropertyActivity
            val showPropertyIntent = Intent(this@AddPropertyActivity, ShowPropertyActivity::class.java)
            startActivity(showPropertyIntent)

        } else {
            val snackbar = Snackbar.make(
                binding.addPropertyLayout,
                "Error: All fields must be filled in",
                Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.holo_red_light
                )
            )
            snackbar.show()
        }
    }
}
