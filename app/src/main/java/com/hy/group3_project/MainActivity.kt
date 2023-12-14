package com.hy.group3_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.hy.group3_project.controllers.properties.PropertyRepository
import com.hy.group3_project.databinding.ActivityMainBinding
import com.hy.group3_project.models.adapters.PropertyAdapter
import com.hy.group3_project.models.properties.FilterData
import com.hy.group3_project.models.properties.Property
import com.hy.group3_project.views.FilterApplyListener
import com.hy.group3_project.views.MyPopup
import com.hy.group3_project.views.properties.PropertyDetailActivity
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private var selectedTextView: TextView? = null
    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val APP_PERMISSIONS_LIST = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val multiplePermissionsResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultsList ->
        Log.d(TAG, resultsList.toString())

        var allPermissionsGrantedTracker = true

        for (item in resultsList.entries) {
            if (item.key in APP_PERMISSIONS_LIST && !item.value) {
                allPermissionsGrantedTracker = false
            }
        }

        if (allPermissionsGrantedTracker) {
            loadUserCurrLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(this.binding.tbOptionMenu)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        this.propertyRepository = PropertyRepository(applicationContext)
        handleTextViewClick(binding.mapText)



        multiplePermissionsResultLauncher.launch(APP_PERMISSIONS_LIST)


        binding.mapText.setOnClickListener {
            handleTextViewClick(binding.mapText)
            mapFragment.view?.visibility = View.VISIBLE
            binding.rvProperties.visibility = View.GONE
            binding.searchButtonForMap.visibility = View.VISIBLE
            binding.searchButtonForList.visibility = View.GONE
        }

        binding.mapList.setOnClickListener {
            handleTextViewClick(binding.mapList)
            mapFragment?.view?.visibility = View.GONE
            binding.rvProperties.visibility = View.VISIBLE
            binding.searchButtonForMap.visibility = View.GONE
            binding.searchButtonForList.visibility = View.VISIBLE
        }

        // Setup adapter

        adapter = PropertyAdapter(
            propertyList,
            { pos -> addToUserList(pos) },
            { pos -> removeFromUserList(pos) },
            { pos -> viewRowDetail(pos) },
            { redirectLogin() },
            user
        )
        binding.rvProperties.adapter = adapter
        binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.rvProperties.visibility = View.GONE
        binding.searchButtonForMap.visibility = View.VISIBLE
        binding.searchButtonForList.visibility = View.GONE


        // -- filter functionality
        binding.filterBtn.setOnClickListener {
            val searchField = binding.searchText
            val myPopup = MyPopup(this, searchField)

            // listener to handle filter events
            myPopup.filterApplyListener = object : FilterApplyListener {
                override fun onFilterApplied(filterData: FilterData) {
                    val filteredPropertiesList = propertyRepository.filterProperties(filterData)
                    // update RV
                    adapter.updateUserPropertyList(filteredPropertiesList)

                }
            }

            myPopup.show()
        }

        binding.searchButtonForList.setOnClickListener {
            val searchText: String = binding.searchText.text.toString()
            val searchedPropertiesList = propertyRepository.searchPropertiesByAddress(searchText)
            // Update RV
            adapter.updateUserPropertyList(searchedPropertiesList)
        }
        // -- Search functionality
        binding.searchButtonForMap.setOnClickListener {

            val geocoder = Geocoder(applicationContext, Locale.getDefault())

            try {
                val cityName = binding.searchText.text.toString()

                if (cityName == "") {
                    val snackbar =
                        Snackbar.make(binding.root, "City name is empty!", Snackbar.LENGTH_LONG)
                    snackbar.show()
                } else {
                    // Try to find the coordinates of the city using Geocoder
                    val addressList: MutableList<Address>? =
                        geocoder.getFromLocationName(cityName, 1)

                    if (addressList != null) {
                        if (addressList.isNotEmpty()) {
                            val address = addressList[0]
                            Log.d(TAG, "address: $address")
                            val lat = address.latitude
                            val lng = address.longitude

                            // Move and zoom the camera on the map
                            mMap?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(lat, lng),
                                    12.0f
                                )
                            )

                            Log.d(TAG, "Latitude: $lat, Longitude: $lng")

                            // Iterate through propertyList and add markers for each property
                            for (property in propertyList) {
                                addMarker(property)
                            }
                        } else {
                            val snackbar = Snackbar.make(
                                binding.root,
                                "City name does not exist!",
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.show()
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error encountered while getting coordinate location.")
                Log.e(TAG, ex.toString())
            }
        }


    }

    private fun loadUserCurrLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            multiplePermissionsResultLauncher.launch(APP_PERMISSIONS_LIST)
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lifecycleScope.launch {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        Log.d(TAG, "Location is null")
                        return@addOnSuccessListener
                    }
                    // later handle null
                    val lat = location.latitude
                    val lng = location.longitude

                    mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 12.0f))

                    // Iterate through propertyList and add markers for each property
                    for (property in propertyList) {

                        addMarker(property)
                    }
                }
        }
    }

    private fun addMarker(property: Property) {
        if (mMap != null) {
            val address = getAddress(property.address)

            if (address != null) {
                val latLng = LatLng(address.latitude, address.longitude)

                val marker = mMap!!.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(property.address)
                )

                // Set a unique tag for each marker
                marker?.tag = property.id // Assuming property.id is unique

                // Set custom info window
                mMap!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                    override fun getInfoWindow(marker: Marker): View? {
                        return null
                    }

                    override fun getInfoContents(marker: Marker): View {
                        val infoView = layoutInflater.inflate(R.layout.marker_property_info, null)

                        val homeTitle = infoView.findViewById<TextView>(R.id.home_address)
                        val homePrice = infoView.findViewById<TextView>(R.id.home_price)
                        val detailActivityLink =
                            infoView.findViewById<TextView>(R.id.link_to_details)

                        // Retrieve the property based on the unique tag
                        val propertyId = marker.tag as String
                        val property = getPropertyById(propertyId)

                        val formattedNumber = String.format("%,d", property.price)

                        homeTitle.text = marker.title
                        homePrice.text = "Price: $$formattedNumber"
                        detailActivityLink.text = "Link to details"

                        val content = SpannableString(detailActivityLink.text)
                        content.setSpan(UnderlineSpan(), 0, content.length, 0)
                        content.setSpan(ForegroundColorSpan(Color.BLUE), 0, content.length, 0)
                        detailActivityLink.text = content

                        mMap!!.setOnInfoWindowClickListener {
                            val intent =
                                Intent(this@MainActivity, PropertyDetailActivity::class.java)
                            // Pass property data to the PropertyDetailActivity using intent
                            intent.putExtra("PROPERTY_ID", property.id)
                            startActivity(intent)
                        }
                        return infoView
                    }
                })

                marker?.showInfoWindow()
            } else {
                Log.e(TAG, "Failed to get valid LatLng for the address")
            }
        } else {
            Log.e(TAG, "Google Map is not available")
            // You can display a message to the user or perform any other action here
        }
    }

    // Helper function to retrieve property by ID
    private fun getPropertyById(propertyId: String): Property {
        // Implement logic to retrieve the property from your data source based on propertyId
        // For example, you can iterate through your propertyList
        return propertyList.first { it.id == propertyId }
    }


    private fun handleTextViewClick(textView: TextView) {
        // Remove styles from the previously clicked TextView
        selectedTextView?.let {
            removeStyles(it)
        }

        // Apply styles to the currently clicked TextView
        applyStyles(textView)

        // Update the selected TextView
        selectedTextView = textView
    }

    private fun applyStyles(textView: TextView) {
        val text = textView.text.toString()
        val spannableString = SpannableString(text)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, 0)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
        textView.text = spannableString
    }

    private fun removeStyles(textView: TextView) {
        val text = textView.text.toString()
        val spannableString = SpannableString(text)
        spannableString.setSpan(StyleSpan(Typeface.NORMAL), 0, text.length, 0)
        spannableString.removeSpan(UnderlineSpan::class.java) // Remove underline span
        textView.text = spannableString
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val defaultLatLng = LatLng(50.000000, -85.000000)
        val defaultZoomLevel = 15.0f
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, defaultZoomLevel))
        loadUserCurrLocation()
    }

    override fun onResume() {
        super.onResume()
        loadUserCurrLocation()
        loadAllData()
    }
    private fun loadAllData() {
        propertyRepository.retrieveAllProperties()
        propertyRepository.allProperties.observe(
            this
        ) { propertiesList ->
            propertyList.clear()
            propertyList.addAll(propertiesList)
            adapter.notifyDataSetChanged()
            mMap?.clear()
            for (property in propertyList) {
                addMarker(property)
            }
        }
    }
}