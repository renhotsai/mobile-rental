package com.hy.group3_project.models.properties

import java.util.UUID

class Property {
    var id:String = UUID.randomUUID().toString()
    lateinit var imageName: String
    lateinit var type: String
    lateinit var beds: String
    lateinit var baths: String
    var petFriendly: Boolean = false
    var canParking: Boolean = false
    var price: Int = 0
    lateinit var desc: String
    lateinit var city: String
    lateinit var address: String
    lateinit var contactInfo: String
    var availability: Boolean = false
    var isFavourite: Boolean = false

    constructor()
    constructor(imageFileName: String, propertyType:String, beds:String, baths:String, petFriendly:Boolean, propertyParking: Boolean,
                propertyPrice: Int, propertyDesc: String, propertyCity: String, propertyAddress: String, contactInfo: String, rentAvailability: Boolean) {
        this.imageName = imageFileName
        this.type = propertyType
        this.beds = beds
        this.baths = baths
        this.petFriendly = petFriendly
        this.canParking = propertyParking
        this.price = propertyPrice
        this.desc = propertyDesc
        this.city = propertyCity
        this.address = propertyAddress
        this.contactInfo = contactInfo
        this.availability = rentAvailability
    }

    override fun toString(): String {
        return "Property(imageName='$imageName', type='$type', beds='$beds', baths='$baths', petFriendly=$petFriendly, canParking=$canParking, price=$price, desc='$desc', city='$city', address='$address', contactInfo='$contactInfo', availability=$availability, isFavourite=$isFavourite)"
    }
}