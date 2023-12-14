package com.hy.group3_project.views.properties

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hy.group3_project.BaseActivity
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.databinding.ActivityAddPropertyBinding
import com.hy.group3_project.models.properties.Property
import kotlin.random.Random

class AddPropertyActivity : BaseActivity() {

    private lateinit var binding: ActivityAddPropertyBinding
    private var savedProperties: MutableList<Property> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbOptionMenu)
        this.propertyRepository = PropertyRepository(applicationContext)
        binding.btnCreate.setBackgroundColor(Color.parseColor("#05a6fc"))

        binding.btnCreate.setOnClickListener {
            val etAddress = binding.editPropertyAddress.text.toString()
            if (etAddress.isNullOrEmpty()) {
                binding.editPropertyAddress.error = "address is empty"
                return@setOnClickListener
            }

            val address = getAddress(etAddress)
            if(address == null){
                Toast.makeText(this@AddPropertyActivity,"Address doesn't exist.",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
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
        val propertyContactInfo = binding.editContactInfo.text.toString()
        val propertyAvailability = binding.swRentAv.isChecked

        if (propertyBeds != "" && propertyPrice != 0 &&
            propertyBaths != "" && propertyDesc != "" && propertyAddress != ""
        ) {

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
                propertyContactInfo,
                propertyAvailability
            )

            propertyRepository.addPropertyToDB(propertyToAdd)


            user!!.addList(propertyToAdd.id)
            Log.d(TAG,"check user add list ${user!!.showList()}")
            userRepository.updateUser(user!!)
            prefEditorUser(user!!)

            val showPropertyIntent =
                Intent(this@AddPropertyActivity, ShowPropertyActivity::class.java)
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
