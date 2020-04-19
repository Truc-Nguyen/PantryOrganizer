package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class PantryData(
    val name: String,
    val location: String,
    val imageLink: String?,
    val foodList: List<FoodData>?)
{
    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        // Construct a list of maps of the food data in this pantry.
        val foodMapList = ArrayList<Map<String, Any?>>()
        if (foodList != null) {
            for (food in foodList) {
                foodMapList.add(food.getDataMap())
            }
        }

        // Return a map of this pantry's data.
        return hashMapOf(
            "name" to name,
            "location" to location,
            "imageLink" to imageLink,
            "foodList" to foodMapList
        )
    }

    // Get the number of unique food types in this pantry.
    fun getFoodTypeCount(): Int {
        return foodList?.size ?: 1
    }

    // Get the total food count in this recipe.
    fun getFoodTotalCount(): Long {
        var total = 0L
        if (foodList != null) {
            for (food in foodList) {
                total += food.quantity
            }
        }

        return total
    }
}

// Create pantry data object from a firebase query snapshot.
fun createPantryDataFromSnapshot(fbDoc: QueryDocumentSnapshot): PantryData {
    // Extract basic pantry data.
    val name = fbDoc["name"] as String
    val location = fbDoc["location"] as String
    val imageLink = fbDoc["imageLink"] as String?

    // Extract food list data in this pantry.
    val foodList = ArrayList<FoodData>()
    if (fbDoc.contains("foodList")) {
        val foodListData = fbDoc["foodList"] as List<Map<String, Any?>>
        for (food in foodListData) {
            foodList.add(FoodData(food))
        }
    }

    // Return the constructed pantry data.
    return PantryData(
        name,
        location,
        imageLink,
        foodList
    )
}