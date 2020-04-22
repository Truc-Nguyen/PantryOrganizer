package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class MealplanData(
    //Using a string to represent date since the firebase date object appears to store things as a number of milliseconds, which seems odd
    //when making a new date object, use the following
    //    val currentDate = LocalDateTime.now()
    //    val currentDateAsString = currentDate.format(DateTimeFormatter.ofPattern("M.d.y"))
    val date: String,
    val recipes: List<RecipeData>?
)
{
    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        // Construct a list of maps of the recipe data for this date.
        val recipeMapList = ArrayList<Map<String, Any?>>()
        if (recipes != null) {
            for (recipe in recipes) {
                recipeMapList.add(recipe.getDataMap())
            }
        }

        // Return a map of this date's data.
        return hashMapOf(
            "date" to date,
            "recipes" to recipeMapList
        )
    }

    // Get the total recipe count for this date
    fun getRecipeTotalCount(): Long {
        var total = 0L
        if (recipes != null) {
            for (recipe in recipes) {
                total += 1
            }
        }

        return total
    }
}

// Create mealplan data object from a firebase query snapshot.
fun createMealplanDataFromSnapshot(fbDoc: QueryDocumentSnapshot): MealplanData {
    // Extract basic mealplan data.
    val date = fbDoc["date"] as String

    // Extract recipe list data in this pantry.
    val recipes = ArrayList<RecipeData>()
    if (fbDoc.contains("recipes")) {
        val recipeListData = fbDoc["recipes"] as List<Map<String, Any?>>
        for (recipe in recipeListData) {
            recipes.add(RecipeData(recipe))
        }
    }

    // Return the constructed mealplan data.
    return MealplanData(
        date,
        recipes
    )
}