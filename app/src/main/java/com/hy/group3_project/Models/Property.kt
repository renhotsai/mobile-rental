package com.hy.group3_project.Models

import java.util.UUID

class Property {
    var id:String
    var imageFileName:String
    var propertyType:String
    var beds: String
    var baths: String
    var petFriendly: Boolean
    var propertyParking: Boolean
    var propertyPrice: Int
    var propertyDesc: String
    var propertyCity: String
    var propertyAddress: String
    var contactInfo: String
    var rentAvailability: Boolean
    var isFavorite:Boolean = false
    constructor(imageFileName: String, propertyType:String, beds:String, baths:String, petFriendly:Boolean, propertyParking: Boolean,
                propertyPrice: Int, propertyDesc: String, propertyCity: String, propertyAddress: String, contactInfo: String, rentAvailability: Boolean) {
        this.id = UUID.randomUUID().toString()
        this.imageFileName = imageFileName
        this.propertyType = propertyType
        this.beds = beds
        this.baths = baths
        this.petFriendly = petFriendly
        this.propertyParking = propertyParking
        this.propertyPrice = propertyPrice
        this.propertyDesc = propertyDesc
        this.propertyCity = propertyCity
        this.propertyAddress = propertyAddress
        this.contactInfo = contactInfo
        this.rentAvailability = rentAvailability
    }

    override fun toString(): String {
        return "Property(id='$id', imageFileName='$imageFileName', propertyType='$propertyType', beds='$beds', baths='$baths', petFriendly=$petFriendly, propertyParking=$propertyParking, propertyPrice=$propertyPrice, propertyDesc='$propertyDesc', propertyCity='$propertyCity', propertyAddress='$propertyAddress', contactInfo='$contactInfo', rentAvailability=$rentAvailability, isFavorite=$isFavorite)"
    }
}