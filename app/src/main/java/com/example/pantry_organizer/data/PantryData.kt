package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class PantryData(
    val name: String,
    val imageLink: String?
)
{
    val foodList = ArrayList<FoodData>()

    // Convenience constructor using a firebase document object.
    constructor(fbDoc: QueryDocumentSnapshot):
            this(fbDoc.get("name") as String, fbDoc.get("imageLink") as String?)

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "imageLink" to imageLink
        )
    }
}