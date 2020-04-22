package com.example.pantry_organizer.global.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.data.*

class AppViewModel(application: Application): AndroidViewModel(application) {
    // Firebase and nutritionIX api repository.
    private val repository = Repository()

    // Live data objects.
    val pantryList = MutableLiveData<List<PantryData>>()
    val apiSearchList = MutableLiveData<ApiFoodDataPayload>()
    val apiFoodNutritionData = MutableLiveData<ApiFoodNutritionPayload>()
    val recipeList = MutableLiveData<List<RecipeData>>()
    val shoppingList = MutableLiveData<List<ShoppingData>>()

    init {
        getPantries()
        getRecipes()
        getShoppingList()
    }


    // GLOBAL //
    // Upload a file to firebase storage. Returns the unique name of the file.
    fun uploadFileToStorage(filePath: String): String {
        return repository.uploadFileToStorage(filePath)
    }


    // PANTRY //
    // Populate pantry list live data from firebase data.
    private fun getPantries() {
        repository.getPantries()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<PantryData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    if (doc.get("name") != null) {
                        list.add(createPantryDataFromSnapshot(doc))
                    }
                }
                pantryList.value = list
            }
    }

    // Push a new pantry to firebase.
    fun addPantry(pantryData: Map<String, Any?>): Boolean {
        // Check for existing pantry with duplicate name.
        if (pantryList.value != null) {
            for (pantry in pantryList.value!!) {
                if (pantry.name == pantryData["name"]) {
                    return false
                }
            }
        }

        // Push the new pantry data to firebase.
        repository.addPantry(pantryData)
        return true
    }

    // Delete a pantry from firebase.
    fun deletePantry(pantryName: String) {
        repository.deletePantry(pantryName)
    }

    // Add a new food data array to the specified pantry.
    fun addFoodToPantry(pantryName: String, foodData: FoodData) {
        repository.addFoodToPantry(pantryName, foodData)
    }

    // Remove a quantity of an existing food from the specified pantry.
    fun removeFoodQtyFromPantry(pantryName: String, foodData: FoodData, quantity: Int) {
        repository.removeFoodQtyFromPantry(pantryName, foodData, quantity)
    }


    // API //
    // Execute an api search on the query.
    fun getApiSearchList(query: String) {
        repository.getApiSearchList(apiSearchList, query)
    }

    // Execute an api retrieval of nutritional data on the query.
    fun getApiFoodNutrition(query: String) {
        repository.getApiFoodNutritionData(apiFoodNutritionData, query)
    }

    
    // RECIPE //
    // Populate recipe list live data from firebase data.
    private fun getRecipes() {
        repository.getRecipes()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<RecipeData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    if (doc.get("name") != null) {
                        list.add(createRecipeDataFromSnapshot(doc))
                    }
                }
                recipeList.value = list
            }
    }

    // Push a new recipe to firebase.
    fun addRecipe(recipeData: Map<String, Any?>): Boolean {
        // Check for existing recipe with duplicate name.
        if (recipeList.value != null) {
            for (recipe in recipeList.value!!) {
                if (recipe.name == recipeData["name"]) {
                    return false
                }
            }
        }

        // Push the new recipe data to firebase.
        repository.addRecipe(recipeData)
        return true
    }

    // Delete a recipe from firebase.
    fun deleteRecipe(recipeName: String) {
        repository.deleteRecipe(recipeName)
    }

    // Add a new food data array to the specified recipe.
    fun addFoodToRecipe(recipeName: String, foodData: FoodData) {
        repository.addFoodToRecipe(recipeName, foodData)
    }

    // Remove a quantity of an existing food from the specified recipe.
    fun removeFoodQtyFromRecipe(recipeName: String, foodData: FoodData, quantity: Int) {
        repository.removeFoodQtyFromRecipe(recipeName, foodData, quantity)
    }

    //Add a quantity of a specific item to the shopping list
    fun addShoppingListItem(shoppingData: Map<String, Any?>): Boolean {
        // Push the new item data to firebase.
        repository.addShoppingListItem(ShoppingData(shoppingData))
        return true
    }

    //Remove a quantity of an existing item form the shopping list
    fun removeShoppingListItem(itemData: ShoppingData, quantity: Int): Boolean {
        // Check for existing pantry with duplicate name.
        // Push the new pantry data to firebase.
        repository.removeItemQtyFromShoppingList(itemData, quantity)
        return true
    }

    private fun getShoppingList() {
        repository.getShoppingList()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<ShoppingData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    if (doc.get("name") != null) {
                        list.add(createShoppingDataFromSnapshot(doc))
                    }
                }
                shoppingList.value = list
            }
    }


}