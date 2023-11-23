package com.hy.group3_project.ViewActivities.Account


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hy.group3_project.Models.Property
import com.hy.group3_project.ViewActivities.BaseActivity

import com.hy.group3_project.databinding.ActivityPropertyDetailBinding
import java.text.NumberFormat

class PropertyDetailActivity : BaseActivity() {
    lateinit var binding:ActivityPropertyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extraItem = intent.getStringExtra("BLOCK_UPDATE_DELETE")

        if (extraItem != null) {
            // Do something with the extra item, e.g., make a button invisible
            binding.btnUpdate.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
        }
        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

        val propertyId = intent.getStringExtra("PROPERTY_ID")

        val propertyListFromSP = sharedPreferences.getString("KEY_PROPERTY_DATASOURCE", "")

        val gson = Gson()
        val typeToken = object: TypeToken<List<Property>>() {}.type
        val propertyList = gson.fromJson<List<Property>>(propertyListFromSP, typeToken)

        val selectedProperty = propertyList.find { it.id == propertyId }

        if (selectedProperty != null) {
            val formattedPrice = NumberFormat.getCurrencyInstance().format(selectedProperty.propertyPrice)
            binding.propertyPrice.text = formattedPrice
            binding.propertyType.text = selectedProperty.propertyType
            binding.bedText.text = selectedProperty.beds
            binding.bathText.text = selectedProperty.baths
            binding.petFriendlyText.text = if (!selectedProperty.petFriendly) "Pets" else "No Pets"
            binding.parkingText.text = if (!selectedProperty.propertyParking) "Parking" else "No Parking"
            binding.addressCity.text = "${selectedProperty.propertyAddress}, ${selectedProperty.propertyCity}"
            binding.propertyDescription.text = selectedProperty.propertyDesc
            binding.contactInfo.text = "Contact Information: ${selectedProperty.contactInfo}"


            if (selectedProperty.rentAvailability) {
                binding.availability.text = "Available"
            } else {
                binding.availability.text = "Not available"
            }

        }

        binding.btnUpdate.setOnClickListener {

            val propertyUpdateIntent = Intent(this@PropertyDetailActivity, UpdatePropertyActivity::class.java)

            propertyUpdateIntent.putExtra("PROPERTY_ID", propertyId)

            startActivity(propertyUpdateIntent)
        }

        binding.btnDelete.setOnClickListener {
            val mutablePropertyList = propertyList.toMutableList()

            val indexToRemove = mutablePropertyList.indexOfFirst { it.id == propertyId }

            if (indexToRemove != -1) {
                mutablePropertyList.removeAt(indexToRemove)

                val jsonPropertyList = gson.toJson(mutablePropertyList)
                prefEditor.putString("KEY_PROPERTY_DATASOURCE", jsonPropertyList)
                prefEditor.apply()

                finish()
            }
        }
    }
}


