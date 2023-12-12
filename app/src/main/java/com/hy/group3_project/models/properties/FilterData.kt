package com.hy.group3_project.models.properties

data class FilterData(
    val propertyType: String?,
    val beds: String?,
    val baths: String?,
    val isPetFriendly: Boolean?,
    val hasParking: Boolean?
) {
    // Secondary constructor with default values
    constructor() : this(null, null, null, null, null)
}