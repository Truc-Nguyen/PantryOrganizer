package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class RecipeData(
    val name: String = "",
    val ingredientsList: String = "",
    val imageLink: String?
)
{

    // Convenience constructor using a firebase document object.
    constructor(fbDoc: QueryDocumentSnapshot):
            this(
                fbDoc.get("name") as String,
                fbDoc.get("ingredientsList") as String,
                fbDoc.get("imageLink") as String?)

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "ingredientsList" to ingredientsList,
            "imageLink" to imageLink
        )
    }
}