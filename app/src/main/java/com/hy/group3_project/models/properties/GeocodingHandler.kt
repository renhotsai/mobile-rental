package com.hy.group3_project.models.properties

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

data class Coordinates(val latitude: Double, val longitude: Double)

data class LocationData(val streetAddress: String, val city: String)

class GeocodingHandler(private val context: Context) {
    private var storedLatitude: Double? = null
    private var storedLongitude: Double? = null

    fun getCoordinates(streetAddress: String): Coordinates? {
        val geocoder = Geocoder(context, Locale.getDefault())

        return try {
            val searchResults: List<Address>? = geocoder.getFromLocationName(streetAddress, 1)

            if (searchResults == null || searchResults.isEmpty()) {
                null
            } else {
                val foundLocation: Address = searchResults[0]
                storedLatitude = foundLocation.latitude
                storedLongitude = foundLocation.longitude
                Coordinates(storedLatitude ?: 0.0, storedLongitude ?: 0.0)
            }

        } catch (e: Exception) {
            null
        }
    }

    fun getCity(latitude: Double, longitude: Double): LocationData {
        val geocoder = Geocoder(context, Locale.getDefault())

        return try {
            val searchResults: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (searchResults == null || searchResults.isEmpty()) {
                LocationData("Location not found", "")
            } else {
                val foundAddress: Address = searchResults[0]
                val streetAddress = foundAddress.thoroughfare ?: ""
                val city = foundAddress.locality ?: ""
                LocationData(streetAddress, city)
            }

        } catch (e: Exception) {
            LocationData("Error getting location", "")
        }
    }
}
