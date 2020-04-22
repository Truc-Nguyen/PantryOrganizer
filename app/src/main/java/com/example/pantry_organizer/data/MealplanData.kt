package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class MealplanData(
    //Using a string to represent date since the firebase date object appears to store things as a number of milliseconds, which seems odd
    //when making a new date object, use the following
    //    val currentDate = LocalDateTime.now()
    //    val currentDateAsString = currentDate.format(DateTimeFormatter.ofPattern("M.d.y"))
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
