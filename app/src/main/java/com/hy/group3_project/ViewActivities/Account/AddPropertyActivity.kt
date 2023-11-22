package com.hy.group3_project.ViewActivities.Account

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hy.group3_project.R
import com.hy.group3_project.databinding.ActivityAddPropertyBinding

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPropertyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_property)

        this.binding.btnCreateProperty.setBackgroundColor(Color.parseColor("#05a6fc"))

        binding.btnCreateProperty.setOnClickListener {
            val propertyType = this.binding.spinnerPropertyType.selectedItem
            val propertyBeds = this.binding.spinnerBeds.selectedItem
            val propertyBaths = this.binding.spinnerBaths.selectedItem
            val propertyPet = this.binding.spinnerPet.selectedItem
            val propertyDesc = this.binding.editPropertyDesc.text.toString()
            val propertyAddress = this.binding.editPropertyAddress.text.toString()

            if (propertyType != null && propertyBeds !=null && propertyBaths != null && propertyPet != null
                && propertyDesc != "" && propertyAddress != "") {

            } else {
                val snackbar = Snackbar.make(binding.addPropertyLayout, "Error: All fields must be filled in", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(ContextCompat.getColor(applicationContext, android.R.color.holo_red_light));

                snackbar.show()
            }
        }
    }
}