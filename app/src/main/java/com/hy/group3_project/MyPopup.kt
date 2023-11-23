package com.hy.group3_project

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup


class MyPopup(context: Context) {

    private val dialog: AlertDialog
    private val view = LayoutInflater.from(context).inflate(R.layout.custom_popup, null)

    // UI elements
    private val propertyTypeGroup: RadioGroup = view.findViewById(R.id.propertyTypeGroup)
    private val bedsGroup: RadioGroup = view.findViewById(R.id.bedsGroup)
    private val bathsGroup: RadioGroup = view.findViewById(R.id.bathsGroup)
    private val checkBoxPetFriendly: CheckBox = view.findViewById(R.id.checkBoxPetFriendly)
    private val checkBoxParking: CheckBox = view.findViewById(R.id.checkBoxParking)

    init {
        val builder = AlertDialog.Builder(context)

        builder.setView(view)

        // Handle positive button click
        builder.setPositiveButton("Apply") { _, _ ->

            val filterConfig = getFilterData()

        }

        // Handle negative button click
        builder.setNegativeButton("Cancel") { _, _ -> }

        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }

    // Function to retrieve filter data from UI elements
    private fun getFilterData(): FilterData {
        val selectedPropertyType = view.findViewById<RadioButton>(propertyTypeGroup.checkedRadioButtonId)?.text.toString()
        val selectedBeds = view.findViewById<RadioButton>(bedsGroup.checkedRadioButtonId)?.tag?.toString()?.toIntOrNull()
        val selectedBaths = view.findViewById<RadioButton>(bathsGroup.checkedRadioButtonId)?.tag?.toString()?.toIntOrNull()
        val isPetFriendly = checkBoxPetFriendly.isChecked
        val hasParking = checkBoxParking.isChecked

        return FilterData(selectedPropertyType, selectedBeds, selectedBaths, isPetFriendly, hasParking)
    }
}