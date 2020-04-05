package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class FoodData(
    var name: String? = null,
    var apiID: Long? = null,
    var imageLink: String?
)
{
    // Convenience constructor using a firebase document object.
    constructor(fbDoc: QueryDocumentSnapshot):
            this(fbDoc.get("name") as String?, fbDoc.get("apiID") as Long?, fbDoc.get("imageLink") as String?)

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "apiID" to apiID,
            "imageLink" to imageLink
        )
    }
}