package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class ShoppingData(
    val name: String,
    var quantity: Long)
{
    // Convenience constructor using a food map.
    constructor(map: Map<String, Any?>): this(
        map["name"] as String,
        map["quantity"] as Long
    )

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "quantity" to quantity
        )
    }
}

fun createShoppingDataFromSnapshot(fbDoc: QueryDocumentSnapshot): ShoppingData {
    // Extract basic pantry data.
    val name = fbDoc["name"] as String
    val quantity = fbDoc["quantity"] as Long

    // Extract food list data in this pantry.
    // Return the constructed pantry data.
    return ShoppingData(
        name,
        quantity
    )
}