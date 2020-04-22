package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlin.math.roundToInt

data class RecipeData(
    val name: String,
    val imageLink: String?,
    val recipeImageLink: String?,
    val rating: Double,
    val foodList: List<FoodData>?)
{
    constructor(map: Map<String, Any?>): this(
        map["name"] as String,
        map["imageLink"] as String?,
        map["recipeImageLink"] as String?,
        map["rating"] as Double,
        map["foodList"] as List<FoodData>?
    )

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
            "imageLink" to imageLink,
            "recipeImageLink" to recipeImageLink,
            "rating" to rating,
            "foodList" to foodMapList
        )
    }

    // Get the number of unique food types in this recipe.
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

    // Get the caloric content of this recipe.
    fun getFoodCalories(): Double {
        var total = 0.0
        if (foodList != null) {
            for (food in foodList) {
                total += food.calories ?: 0.0 * food.quantity
            }
        }

        return (total * 100).roundToInt() / 100.0
    }
}

// Create recipe data object from a firebase query snapshot.
fun createRecipeDataFromSnapshot(fbDoc: QueryDocumentSnapshot): RecipeData {
    // Extract basic recipe data.
    val name = fbDoc["name"] as String
    val imageLink = fbDoc["imageLink"] as String?
    val recipeImageLink = fbDoc["recipeImageLink"] as String?
    val rating = fbDoc["rating"] as Double

    // Extract food list data in this recipe.
    val foodList = ArrayList<FoodData>()
    if (fbDoc.contains("foodList")) {
        val foodListData = fbDoc["foodList"] as List<Map<String, Any?>>
        for (food in foodListData) {
            foodList.add(FoodData(food))
        }
    }

    // Return the constructed pantry data.
    return RecipeData(
        name,
        imageLink,
        recipeImageLink,
        rating,
        foodList
    )
}