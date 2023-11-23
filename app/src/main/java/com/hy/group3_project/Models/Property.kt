package com.hy.group3_project.Models

class Property {
    var imageFileName:String
    var propertyType:String
    var beds: String
    var baths: String
    var petFriendly: Boolean
    var propertyPrice: Int
    var propertyDesc: String
    var propertyAddress: String
    var rentAvailability: Boolean
    constructor(imageFileName: String, propertyType:String, beds:String, baths:String, petFriendly:Boolean,
                propertyPrice: Int, propertyDesc: String, propertyAddress: String, rentAvailability: Boolean) {
        this.imageFileName = imageFileName
        this.propertyType = propertyType
        this.beds = beds
        this.baths = baths
        this.petFriendly = petFriendly
        this.propertyPrice = propertyPrice
        this.propertyDesc = propertyDesc
        this.propertyAddress = propertyAddress
        this.rentAvailability = rentAvailability
    }

    override fun toString(): String {
        return super.toString()
    }
}