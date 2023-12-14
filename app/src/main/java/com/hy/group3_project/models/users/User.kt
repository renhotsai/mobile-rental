package com.hy.group3_project.models.users

import android.util.Log
import com.hy.group3_project.models.enums.EditAccountStatus
import com.hy.group3_project.models.enums.ResponseEnum
import java.io.Serializable


class User : Serializable {

    lateinit var id: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var role: String
    var properties: MutableList<String> = mutableListOf()

    constructor()
    constructor(
        id: String,
        firstName: String,
        lastName: String,
        role: String,
        ){
        this.id= id
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
    }

    fun changeAcctInfo(firstName: String, lastName: String): EditAccountStatus {
        if (!firstName.isNullOrEmpty()) {
            this.firstName = firstName
        }
        if (!lastName.isNullOrEmpty()) {
            this.lastName = lastName
        }
        return EditAccountStatus.Success
    }

    fun showList(): MutableList<String> {
        return this.properties
    }

    fun addList(item: String): ResponseEnum {
        return try {
            this.properties.add(item)
            ResponseEnum.Success
        } catch (ex: Exception) {
            Log.e("Tenant", "$ex")
            ResponseEnum.Fail
        }
    }

    fun removeList(propertyId: String): ResponseEnum {
        try {
            val property = properties.find { it == propertyId }
            val index = properties.indexOf(property)
            this.properties.removeAt(index)
            return ResponseEnum.Success
        } catch (ex: Exception) {
            Log.e("Tenant", "$ex")
            return ResponseEnum.Fail
        }
    }

    override fun toString(): String {
        return "User(id='$id', firstName='$firstName', lastName='$lastName', role='$role', properties=$properties)"
    }
}