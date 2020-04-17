package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class PantryData(
    val name: String,
    val location: String,
    val imageLink: String?,
    val foodList: List<List<String>>? // = emptyList()
)
{
    // Convenience constructor using a firebase document object.
    constructor(fbDoc: QueryDocumentSnapshot):
            this(
                fbDoc.get("name") as String,
                fbDoc.get("location") as String,
                fbDoc.get("imageLink") as String?,
                fbDoc.get("foodList") as List<List<String>>? //uncertain if correct
                )

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "location" to location,
            "imageLink" to imageLink ,
            "foodList" to foodList //uncertain nif this works for Lists too
        )
    }
}