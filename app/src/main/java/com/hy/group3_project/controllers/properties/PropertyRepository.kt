package com.hy.group3_project.controllers.properties

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.firestore
import com.hy.group3_project.models.properties.FilterData
import com.hy.group3_project.models.properties.Property
import kotlinx.coroutines.tasks.await

class PropertyRepository(private val context: Context) {

    private val TAG = this.toString();

    //get an instance of firestore database
    private val db = Firebase.firestore

    private val COLLECTION_PROPERTIES = "Properties";

    var FIELD_ID = "id"
    var FIELD_IMAGE_NAME = "imageName"
    var FIELD_TYPE = "type"
    var FIELD_BEDS = "beds"
    var FIELD_BATHS = "baths"
    var FIELD_PET_FRIENDLY = "petFriendly"
    var FIELD_CAN_PARKING = "canParking"
    var FIELD_PRICE = "price"
    var FIELD_DESC = "desc"
    var FIELD_CITY = "city"
    var FIELD_ADDRESS = "address"
    var FIELD_CONTACT_INFO = "contactInfo"
    var FIELD_AVAILABILITY = "availability"
    var FIELD_IS_FAVOURITE = "isFavourite"


    var allProperties: MutableLiveData<List<Property>> = MutableLiveData<List<Property>>()
    var userProperties:MutableLiveData<List<Property>> = MutableLiveData<List<Property>>()


//    private var loggedInUserEmail = ""
//    private var sharedPrefs : SharedPreferences

    //    init {
//        sharedPrefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
//
//        if (sharedPrefs.contains("USER_EMAIL")){
//            loggedInUserEmail = sharedPrefs.getString("USER_EMAIL", "NA").toString()
//        }
//    }
    fun addPropertyToDB(newProperty: Property) {
        try {
            val data: MutableMap<String, Any> = HashMap();

            data[FIELD_ID] = newProperty.id
            data[FIELD_IMAGE_NAME] = newProperty.imageName
            data[FIELD_TYPE] = newProperty.type
            data[FIELD_BEDS] = newProperty.beds
            data[FIELD_BATHS] = newProperty.baths
            data[FIELD_PET_FRIENDLY] = newProperty.petFriendly
            data[FIELD_CAN_PARKING] = newProperty.canParking
            data[FIELD_PRICE] = newProperty.price
            data[FIELD_DESC] = newProperty.desc
            data[FIELD_CITY] = newProperty.city
            data[FIELD_ADDRESS] = newProperty.address
            data[FIELD_CONTACT_INFO] = newProperty.contactInfo
            data[FIELD_AVAILABILITY] = newProperty.availability
            data[FIELD_IS_FAVOURITE] = newProperty.isFavourite

            //for adding document to nested collection
            db.collection(COLLECTION_PROPERTIES).document(newProperty.id).set(data)
                .addOnSuccessListener { docRef ->
                    Log.d(
                        TAG,
                        "addPropertyToDB: Document successfully added with ID : ${newProperty.id}"
                    )
                }.addOnFailureListener { ex ->
                    Log.e(
                        TAG,
                        "addPropertyToDB: Exception occurred while adding a document : $ex",
                    )
                }
        } catch (ex: java.lang.Exception) {
            Log.d(
                TAG,
                "addPropertyToDB: Couldn't perform insert on Properties collection due to exception $ex"
            )
        }
    }

    fun retrieveAllProperties() {
        try {
            db.collection(COLLECTION_PROPERTIES)
                .addSnapshotListener(EventListener { result, error ->
                    if (error != null) {
                        Log.e(
                            TAG,
                            "retrieveAllProperties: Listening to Expenses collection failed due to error : $error",
                        )
                        return@EventListener
                    }

                    if (result != null) {
                        Log.d(
                            TAG,
                            "retrieveAllProperties: Number of documents retrieved : ${result.size()}"
                        )

                        val tempList: MutableList<Property> = ArrayList<Property>()

                        for (docChanges in result.documentChanges) {

                            val currentDocument: Property =
                                docChanges.document.toObject(Property::class.java)
                            Log.d(
                                TAG, "retrieveAllProperties: currentDocument : $currentDocument."

                            )

                            when (docChanges.type) {
                                DocumentChange.Type.ADDED -> {
                                    //do necessary changes to your local list of objects
                                    tempList.add(currentDocument)
                                }

                                DocumentChange.Type.MODIFIED -> {

                                }

                                DocumentChange.Type.REMOVED -> {

                                }
                            }
                        }//for
                        Log.d(TAG, "retrieveAllProperties: tempList : $tempList")
                        //replace the value in allExpenses

                        allProperties.postValue(tempList)

                    } else {
                        Log.d(
                            TAG, "retrieveAllProperties: No data in the result after retrieving"
                        )
                    }
                })


        } catch (ex: java.lang.Exception) {
            Log.e(TAG, "retrieveAllProperties: Unable to retrieve all expenses : $ex")
        }
    }

    suspend fun findProperty(propertyId: String):Property? {
        return try {
            val documentSnapshot = db.collection(COLLECTION_PROPERTIES)
                .document(propertyId)
                .get().await()
            val property = documentSnapshot.toObject(Property::class.java)
            property
        } catch (ex: java.lang.Exception) {
            Log.e(TAG, "filterExpenses: Unable to filter expenses : $ex")
            null
        }
    }

    fun updateProperty(propertyToUpdate: Property) {
        val data: MutableMap<String, Any> = HashMap();

        data[FIELD_ID] = propertyToUpdate.id
        data[FIELD_IMAGE_NAME] = propertyToUpdate.imageName
        data[FIELD_TYPE] = propertyToUpdate.type
        data[FIELD_BEDS] = propertyToUpdate.beds
        data[FIELD_BATHS] = propertyToUpdate.baths
        data[FIELD_PET_FRIENDLY] = propertyToUpdate.petFriendly
        data[FIELD_CAN_PARKING] = propertyToUpdate.canParking
        data[FIELD_PRICE] = propertyToUpdate.price
        data[FIELD_DESC] = propertyToUpdate.desc
        data[FIELD_CITY] = propertyToUpdate.city
        data[FIELD_ADDRESS] = propertyToUpdate.address
        data[FIELD_CONTACT_INFO] = propertyToUpdate.contactInfo
        data[FIELD_AVAILABILITY] = propertyToUpdate.availability
        data[FIELD_IS_FAVOURITE] = propertyToUpdate.isFavourite

        try {
            db.collection(COLLECTION_PROPERTIES).document(propertyToUpdate.id).update(data)
                .addOnSuccessListener { docRef ->
                    Log.d(TAG, "updateProperty: Document updated successfully : $docRef")
                }.addOnFailureListener { ex ->
                    Log.e(TAG, "updateProperty: Failed to update document : $ex")
                }
        } catch (ex: Exception) {
            Log.e(TAG, "updateProperty: Unable to update property due to exception : $ex")
        }
    }

    fun deleteProperty(propertyToDelete: Property) {
        try {
            db.collection(COLLECTION_PROPERTIES).document(propertyToDelete.id).delete()
                .addOnSuccessListener { docRef ->
                    Log.d(TAG, "deleteProperty: Document deleted successfully : $docRef")
                }.addOnFailureListener { ex ->
                    Log.e(TAG, "deleteProperty: Failed to delete document : $ex")
                }
        } catch (ex: Exception) {
            Log.e(TAG, "deleteProperty: Unable to delete expense due to exception : $ex")
        }
    }

    fun getPropertiesWithId(userPropertyList: MutableList<String>) {
        try {
            db.collection(COLLECTION_PROPERTIES)
                .whereIn(FIELD_ID,userPropertyList)
                .addSnapshotListener(EventListener { result, error ->
                    if (error != null) {
                        Log.e(
                            TAG,
                            "getPropertiesWithId: Listening to Expenses collection failed due to error : $error",
                        )
                        return@EventListener
                    }

                    if (result != null) {
                        Log.d(
                            TAG,
                            "getPropertiesWithId: Number of documents retrieved : ${result.size()}"
                        )

                        val tempList: MutableList<Property> = ArrayList<Property>()

                        for (docChanges in result.documentChanges) {

                            val currentDocument: Property =
                                docChanges.document.toObject(Property::class.java)
                            Log.d(
                                TAG, "getPropertiesWithId: currentDocument : $currentDocument."

                            )

                            when (docChanges.type) {
                                DocumentChange.Type.ADDED -> {
                                    //do necessary changes to your local list of objects
                                    tempList.add(currentDocument)
                                }

                                DocumentChange.Type.MODIFIED -> {

                                }

                                DocumentChange.Type.REMOVED -> {

                                }
                            }
                        }//for
                        Log.d(TAG, "getPropertiesWithId: tempList : $tempList")
                        //replace the value in allExpenses

                        userProperties.postValue(tempList)

                    } else {
                        Log.d(
                            TAG, "getPropertiesWithId: No data in the result after retrieving"
                        )
                    }
                })
        } catch (ex: java.lang.Exception) {
            Log.e(TAG, "getPropertiesWithId: Unable to retrieve all expenses : $ex")
        }
    }

    // for filter
    fun filterProperties(filterData: FilterData): List<Property> {
        val filteredProperties = allProperties.value?.filter { property ->
            (filterData.propertyType.isNullOrBlank() || property.type == filterData.propertyType) &&
                    (filterData.beds.isNullOrBlank() || property.beds == filterData.beds) &&
                    (filterData.baths.isNullOrBlank() || property.baths == filterData.baths) &&
                    (filterData.isPetFriendly == null || property.petFriendly == filterData.isPetFriendly) &&
                    (filterData.hasParking == null || property.canParking == filterData.hasParking) &&
                    (filterData.searchField.isNullOrBlank() || property.address?.contains(filterData.searchField, ignoreCase = true) == true)
        }

        return filteredProperties ?: emptyList()
    }

    // for search

    fun searchPropertiesByAddress(searchInput: String): List<Property> {
        val filteredProperties = allProperties.value?.filter { property ->
            property.address?.contains(searchInput, ignoreCase = true) == true
        }

        return filteredProperties ?: emptyList()
    }



}