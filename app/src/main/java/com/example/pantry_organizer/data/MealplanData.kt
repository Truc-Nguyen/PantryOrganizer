package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class MealplanData(
    //when making a new date object, use Date() and then convert that to a string using SimpleDateFormat
    val date: String,
    val recipes: List<String>?
)
{

    // Convenience constructor using a firebase document object.
//    constructor(fbDoc: QueryDocumentSnapshot):
//            this(
//                fbDoc.get("date") as String,
//                fbDoc.get("recipes") as List<String>
//            )

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to date,
            "recipes" to recipes
        )
    }
}
