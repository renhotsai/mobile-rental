package com.hy.group3_project

class FilterData(
    val propertyType: String?,
    val beds: Int?,
    val baths: Int?,
    val isPetFriendly: Boolean?,
    val hasParking: Boolean?
) {
    // Secondary constructor with default values
    constructor() : this(null, null, null, null, null)
}