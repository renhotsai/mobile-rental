package com.hy.group3_project.ViewActivities.Account

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.hy.group3_project.Models.Property
import com.hy.group3_project.R
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityAddPropertyBinding
import kotlin.random.Random

class AddPropertyActivity : BaseActivity() {

    private lateinit var binding: ActivityAddPropertyBinding
    var savedProperties: MutableList<Property> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        this.binding.btnCreate.setBackgroundColor(Color.parseColor("#05a6fc"))

        binding.btnCreate.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val propertyType = this.binding.spinnerPropertyType.selectedItem.toString()
        val propertyBeds = this.binding.spinnerBeds.selectedItem.toString()
        val propertyBaths = this.binding.spinnerBaths.selectedItem.toString()
        val propertyPet = this.binding.spinnerPet.selectedItem.toString().toBoolean()
        val propertyParking = this.binding.spinnerParking.selectedItem.toString().toBoolean()
        val propertyPrice = this.binding.editPropertyPrice.text.toString().toInt()
        val propertyDesc = this.binding.editPropertyDesc.text.toString()
        val propertyCity = this.binding.editPropertyCity.text.toString()
        val propertyAddress = this.binding.editPropertyAddress.text.toString()
        val propertyAvailability = this.binding.swRentAv.isChecked

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

            // Add this property to the list of properties
            savedProperties.add(propertyToAdd)

            // Save this list of properties back to SharedPreferences
            val gson = Gson()
            val listAsString = gson.toJson(savedProperties)
            this.prefEditor.putString("KEY_PROPERTY_DATASOURCE", listAsString)

            // Commit the changes made to SharedPreferences
            this.prefEditor.apply()

            // Start the ShowPropertyActivity
            val showPropertyIntent = Intent(this@AddPropertyActivity, ShowPropertyActivity::class.java)
            startActivity(showPropertyIntent)

        } else {
            val snackbar =
                Snackbar.make(
                    binding.addPropertyLayout,
                    "Error: All fields must be filled in",
                    Snackbar.LENGTH_LONG
                )
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.holo_red_light
                )
                snackbar.show()
            }
        }
    }
}
