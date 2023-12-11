package com.hy.group3_project.views.properties


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.databinding.ActivityPropertyDetailBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat

class PropertyDetailActivity : BaseActivity() {
    val TAG = this.localClassName
    lateinit var binding: ActivityPropertyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.propertyRepository = PropertyRepository(applicationContext)
//set option menu

        val extraItem = intent.getStringExtra("BLOCK_UPDATE_DELETE")

        if (extraItem != null) {
            // Do something with the extra item, e.g., make a button invisible
            binding.btnUpdate.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
        }
        //set option menu

        setSupportActionBar(this.binding.tbOptionMenu)

        val propertyId = intent.getStringExtra("PROPERTY_ID")!!
        lifecycleScope.launch {
            val selectedProperty = propertyRepository.findProperty(propertyId)

            if (selectedProperty != null) {
                val formattedPrice =
                    NumberFormat.getCurrencyInstance().format(selectedProperty.price)
                binding.propertyPrice.text = formattedPrice
                binding.propertyType.text = selectedProperty.type
                binding.bedText.text = selectedProperty.beds
                binding.bathText.text = selectedProperty.baths
                binding.petFriendlyText.text =
                    if (!selectedProperty.petFriendly) "Pets" else "No Pets"
                binding.parkingText.text =
                    if (!selectedProperty.canParking) "Parking" else "No Parking"
                binding.addressCity.text =
                    "${selectedProperty.address}, ${selectedProperty.city}"
                binding.propertyDescription.text = selectedProperty.desc
                binding.contactInfo.text = "Contact: ${selectedProperty.contactInfo}"


                if (selectedProperty.availability) {
                    binding.availability.text = "Available"
                } else {
                    binding.availability.text = "Not available"
                }
                val latLng = getLatLng(selectedProperty.address)
                if (latLng != null) {

                }
            }
        }


        binding.btnUpdate.setOnClickListener {

            val propertyUpdateIntent =
                Intent(this@PropertyDetailActivity, UpdatePropertyActivity::class.java)

            propertyUpdateIntent.putExtra("PROPERTY_ID", propertyId)

            startActivity(propertyUpdateIntent)
        }

        binding.btnDelete.setOnClickListener {
            val propertyId = intent.getStringExtra("PROPERTY_ID")!!
            lifecycleScope.launch {
                val deleteProperty = propertyList.find { it.id == propertyId }
                if (deleteProperty != null) {
                    propertyRepository.deleteProperty(deleteProperty)
                }
                finish()
            }
        }
    }
}


