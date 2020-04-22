package com.example.pantry_organizer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.api.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.util.*

class Repository {
    // Set firebase instances.
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val fbs = Firebase.storage.reference
    private val userID = auth.currentUser?.uid


    // GLOBAL //
    // Upload a file into firebase storage.
    fun uploadFileToStorage(filePath: String): String {
        // Generate a unique file name for this file.
        val fbsFilename = "${UUID.randomUUID()}.jpg"

        // Upload the file into firebase storage with the unique name.
        fbs.child(fbsFilename).putStream(FileInputStream(File(filePath)))

        return fbsFilename
    }


    // PANTRY //
    // Get pantry list from firebase.
    fun getPantries(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
    }

    // Push new pantry to firebase.
    fun addPantry(pantryData: Map<String, Any?>){
        db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryData["name"] as String)
            .set(pantryData)
    }

    // Delete a pantry from firebase.
    fun deletePantry(pantryName: String) {
        db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryName)
            .delete()
    }

    // Push food to pantry in firebase.
    fun addFoodToPantry(pantryName: String, foodData: FoodData) {
        // Create a reference to the pantry firebase document.
        val pantryDocRef = db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryName)

        // Inspect the pantry data.
        pantryDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the pantry.
            if (it.contains("foodList")) {
                for (dbFood in it["foodList"] as List<Map<String, Any?>>) {
                    if (dbFood["name"] == foodData.name) {
                        // Food already exists in pantry. Update the quantity.
                        foodData.quantity += dbFood["quantity"] as Long

                        // Delete the old food data.
                        pantryDocRef.update("foodList", FieldValue.arrayRemove(dbFood))
                    }
                }
            }

            // Add the food data to the pantry.
            pantryDocRef.update("foodList", FieldValue.arrayUnion(foodData.getDataMap()))
        }
    }

    // Remove a quantity of an existing food from the specified pantry in firebase.
    fun removeFoodQtyFromPantry(pantryName: String, foodData: FoodData, quantity: Int) {
        // Create a reference to the pantry firebase document.
        val pantryDocRef = db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryName)

        // Inspect the pantry data.
        pantryDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the pantry.
            if (it.contains("foodList")) {
                for (dbFood in it["foodList"] as List<Map<String, Any?>>) {
                    if (dbFood["name"] == foodData.name) {
                        // Update the food quantity.
                        foodData.quantity = dbFood["quantity"] as Long - quantity

                        // Delete the old food data.
                        pantryDocRef.update("foodList", FieldValue.arrayRemove(dbFood))

                        // Add the new food data with the updated quantity only if the quantity is non-zero.
                        if (foodData.quantity > 0) {
                            pantryDocRef.update("foodList", FieldValue.arrayUnion(foodData.getDataMap()))
                        }
                    }
                }
            }
        }
    }


    // API //
    // Define retrofit service.
    private val service = ApiClient.makeRetrofitService()

    // Co-routine for fetching an api search list on the query.
    fun getApiSearchList(resBody: MutableLiveData<ApiFoodDataPayload>, query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFoodBySearch(query)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        resBody.value = response.body()
                    }
                } catch (e: HttpException) {
                    println("Http error")
                }
            }
        }
    }

    // Co-routine for fetching api nutrition data on the query.
    fun getApiFoodNutritionData(resBody: MutableLiveData<ApiFoodNutritionPayload>, query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFoodNutrients(query)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        resBody.value = response.body()
                    }
                } catch (e: HttpException) {
                    println("Http error")
                }
            }
        }
    }
    
    
    // RECIPE //
    // Get recipe list from firebase.
    fun getRecipes(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
    }

    // Push new recipe to firebase.
    fun addRecipe(recipeData: Map<String, Any?>){
        db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeData["name"] as String)
            .set(recipeData)
    }

    // Delete a recipe from firebase.
    fun deleteRecipe(recipeName: String) {
        db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)
            .delete()
    }

    // Push food to recipe in firebase.
    fun addFoodToRecipe(recipeName: String, foodData: FoodData) {
        // Create a reference to the recipe firebase document.
        val recipeDocRef = db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)

        // Inspect the recipe data.
        recipeDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the recipe.
            if (it.contains("foodList")) {
                for (dbFood in it["foodList"] as List<Map<String, Any?>>) {
                    if (dbFood["name"] == foodData.name) {
                        // Food already exists in recipe. Update the quantity.
                        foodData.quantity += dbFood["quantity"] as Long

                        // Delete the old food data.
                        recipeDocRef.update("foodList", FieldValue.arrayRemove(dbFood))
                    }
                }
            }

            // Add the food data to the recipe.
            recipeDocRef.update("foodList", FieldValue.arrayUnion(foodData.getDataMap()))
        }
    }

    // Remove a quantity of an existing food from the specified recipe in firebase.
    fun removeFoodQtyFromRecipe(recipeName: String, foodData: FoodData, quantity: Int) {
        // Create a reference to the recipe firebase document.
        val recipeDocRef = db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)

        // Inspect the recipe data.
        recipeDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the recipe.
            if (it.contains("foodList")) {
                for (dbFood in it["foodList"] as List<Map<String, Any?>>) {
                    if (dbFood["name"] == foodData.name) {
                        // Update the food quantity.
                        foodData.quantity = dbFood["quantity"] as Long - quantity

                        // Delete the old food data.
                        recipeDocRef.update("foodList", FieldValue.arrayRemove(dbFood))

                        // Add the new food data with the updated quantity only if the quantity is non-zero.
                        if (foodData.quantity > 0) {
                            recipeDocRef.update("foodList", FieldValue.arrayUnion(foodData.getDataMap()))
                        }
                    }
                }
            }
        }
    }

    // MEALPLAN //
    // Get dates list from firebase.
    fun getDates(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("Dates")
    }

    // Push new date to firebase.
    fun addDate(mealPlanData: Map<String, Any?>){
        db.collection("userData")
            .document(userID!!)
            .collection("Dates")
            .document(mealPlanData["date"] as String)
            .set(mealPlanData)
    }

    fun addRecipeToDate (date: String, recipeName: String) {
        // Create a reference to the date firebase document
//        var tmp: RecipeData? = null
        val recipeDocRef = db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)

        recipeDocRef.get().addOnSuccessListener { snapshot ->
            val tmp = RecipeData(
                snapshot["name"] as String,
                snapshot["imageLink"] as String?,
                snapshot["recipeImageLink"] as String?,
                snapshot["rating"] as Double,
                snapshot["foodList"] as List<FoodData>?
            )
            Log.d("viewmodeltmp",tmp.name)
            // Inspect the date data.
            val dateDocRef = db.collection("userData")
                .document(userID!!)
                .collection("Dates")
                .document(date)

            dateDocRef.get().addOnSuccessListener {
                Log.d("viewmodeldateobj", it["recipes"].toString())
                val list: MutableList<RecipeData> = mutableListOf()
                for (recipe in it.get("recipes") as List<Map<String,Any>>){
                    list.add(RecipeData(recipe))
                }
                list.add(tmp)

                // Add the recipe data to the date.
                dateDocRef.update("recipes", list ) //as List<Map<String,Any>>
                //Log.d("viewmodeldateobj", it["recipes"].toString())
            }
        }

    }
    fun removeRecipeFromDate(date: String, recipeData: RecipeData) {
        // Create a reference to the date firebase document.
        val dateDocRef = db.collection("userData")
            .document(userID!!)
            .collection("mealplanDates")
            .document(date)

        // Inspect the date data.
        dateDocRef.get().addOnSuccessListener {
            // Check if the recipe already exists in the date.
            if (it.contains("recipes")) {
                for (dbRecipe in it["recipes"] as List<Map<String, Any?>>) {
                    if (dbRecipe["name"] == recipeData.name) {
                        // Delete the old recipe data.
                        dateDocRef.update("recipes", FieldValue.arrayRemove(dbRecipe))
                    }
                }
            }
        }
    }
    fun getRecipesForDate(date: String, resBody: MutableLiveData<List<RecipeData>>) {
        // Create a reference to the date firebase document.
        val parsedDate = date.split("/") //MM/DD/YYYY

        val dateDocRef = db.collection("userData")
            .document(userID!!)
            .collection("Dates")
            .document(date)
        dateDocRef.get().addOnSuccessListener {
            //update resbody with recipes
            if (it["recipes"] != null){
                val list: MutableList<RecipeData> = mutableListOf()
                for (recipe in it.get("recipes") as List<Map<String,Any>>){
                    list.add(RecipeData(recipe))
                }
                resBody.value = list
            }
            else {
                resBody.value = emptyList()
            }

        }
    }

    //get shopping list from firebase
    fun getShoppingList(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("shoppingList")
    }

//    fun addPantry(pantryData: Map<String, Any?>){
//        db.collection("userData")
//            .document(userID!!)
//            .collection("pantryList")
//            .document(pantryData["name"] as String)
//            .set(pantryData)
//    }

    // Push food to shopping list in firebase.
    fun addShoppingListItem(shoppingData: ShoppingData) {
        // Create a reference to the shopping firebase document.
        val shoppingDocRef = db.collection("userData")
            .document(userID!!)
            .collection("shoppingList")
            .document()

        // Inspect the pantry data.
        shoppingDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the shopping list.
            if (it.contains("shoppingList")) {
                for (dbItem in it["shoppingList"] as List<Map<String, Any?>>) {
                    if (dbItem["name"] == shoppingData.name) {
                        // Food already exists in pantry. Update the quantity.
                        shoppingData.quantity += dbItem["quantity"] as Long
                        // Delete the old food data.
                        shoppingDocRef.update("foodList", FieldValue.arrayRemove(dbItem))
                    }
                }
            }
            // Add the food data to the pantry.
            shoppingDocRef.update("shoppingList", FieldValue.arrayUnion(shoppingData.getDataMap()))
        }
    }

    // Remove a quantity of an existing food from the shopping list in firebase.
    fun removeItemQtyFromShoppingList(itemData: ShoppingData, quantity: Int) {
        // Create a reference to the recipe firebase document.
        val recipeDocRef = db.collection("userData")
            .document(userID!!)
            .collection("shoppingList")
            .document()

        // Inspect the recipe data.
        recipeDocRef.get().addOnSuccessListener {
            // Check if the item already exists in the shopping list
            if (it.contains("shoppingList")) {
                for (dbItem in it["shoppingList"] as List<Map<String, Any?>>) {
                    if (dbItem["name"] == itemData.name) {
                        // Update the item quantity.
                        itemData.quantity = dbItem["quantity"] as Long - quantity

                        // Delete the old food data.
                        recipeDocRef.update("shoppingList", FieldValue.arrayRemove(dbItem))

                        // Add the new shopping data with the updated quantity only if the quantity is non-zero.
                        if (itemData.quantity > 0) {
                            recipeDocRef.update("shoppingList", FieldValue.arrayUnion(itemData.getDataMap()))
                        }
                    }
                }
            }
        }
    }



}

