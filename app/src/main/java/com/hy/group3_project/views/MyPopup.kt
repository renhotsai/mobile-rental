package com.hy.group3_project.views

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.hy.group3_project.models.properties.FilterData
import com.hy.group3_project.R

interface FilterApplyListener {
    fun onFilterApplied(filterData: FilterData)
}


class MyPopup(context: Context, searchFieldMain: EditText) {

    private val dialog: AlertDialog
    private val view = LayoutInflater.from(context).inflate(R.layout.custom_popup, null)

    // UI elements
    private val spinnerPropertyType: Spinner = view.findViewById(R.id.spinnerPropertyType)
    private val spinnerBeds: Spinner = view.findViewById(R.id.spinnerBeds)
    private val spinnerBaths: Spinner = view.findViewById(R.id.spinnerBaths)
    private val checkBoxPetFriendly: CheckBox = view.findViewById(R.id.checkBoxPetFriendly)
    private val checkBoxParking: CheckBox = view.findViewById(R.id.checkBoxParking)
    private val searchFieldFilter: EditText = view.findViewById(R.id.filter_search_text)
    private val searchFieldMain: EditText = searchFieldMain

    var isApplied = false
    lateinit var filterConfig: FilterData
    var filterApplyListener: FilterApplyListener? = null


    init {
        val builder = AlertDialog.Builder(context)

        builder.setView(view)
        searchFieldFilter.text = searchFieldMain.text

        // Handle positive button click
        builder.setPositiveButton("Apply") { _, _ ->
            isApplied = true
            filterConfig = getFilterData()
            filterApplyListener?.onFilterApplied(filterConfig)
        }

        // Handle negative button click
        builder.setNegativeButton("Cancel") { _, _ ->
            isApplied = false
            resetFilterFields()
        }

        // Set the OnDismissListener to handle dismiss events
        builder.setOnDismissListener {
            isApplied = false
            resetFilterFields()
        }

        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }

    private fun resetFilterFields() {
        // Clear the checked checkboxes and reset spinners to default selection
        checkBoxPetFriendly.isChecked = false
        checkBoxParking.isChecked = false
        spinnerPropertyType.setSelection(0)
        spinnerBeds.setSelection(0)
        spinnerBaths.setSelection(0)
        searchFieldFilter.text = null
    }

    // Function to retrieve filter data from UI elements
    private fun getFilterData(): FilterData {
        val selectedPropertyType = getSelectedSpinnerItem(spinnerPropertyType)
        val selectedBeds = getSelectedSpinnerItem(spinnerBeds)
        val selectedBaths = getSelectedSpinnerItem(spinnerBaths)
        val isPetFriendly = checkBoxPetFriendly.isChecked
        val hasParking = checkBoxParking.isChecked
        val searchInput = searchFieldFilter.text.toString()

        return FilterData(selectedPropertyType, selectedBeds, selectedBaths, isPetFriendly, hasParking,searchInput)
    }

    // Function to get selected item from a spinner
// Function to get selected item from a spinner
    private fun getSelectedSpinnerItem(spinner: Spinner): String {
        val selectedItem = spinner.selectedItem.toString()
        return if (selectedItem.contains("Select")) "" else selectedItem
    }

}
