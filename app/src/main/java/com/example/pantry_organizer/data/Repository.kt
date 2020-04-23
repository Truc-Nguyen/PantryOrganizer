package com.example.pantry_organizer.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.lang.Long.min
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

    fun getRecipeIngredients(recipeName: String, ingredients: MutableLiveData<List<FoodData>>){
        val recipeDocRef = db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)


        recipeDocRef.get().addOnSuccessListener {
            val foodMapList = it["foodList"]
            val foodList: MutableList<FoodData> = mutableListOf()
            if(foodMapList != null){
                for(food in foodMapList as List<Map<String,Any>>){
                    foodList.add(FoodData(food))
                }
            }
            ingredients.value = foodList
        }
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

    // Update the recipe rating.
    fun updateRecipeRating(recipeName: String, rating: Float) {
        // Create a reference to the recipe firebase document.
        val recipeDocRef = db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)

        // Update the recipe rating.
        recipeDocRef.get().addOnSuccessListener {
            recipeDocRef.update("rating", rating)
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
                snapshot["foodList"] as List<FoodData>?)

            //use helper to correctly format data
            val data = recipeDateHelper(tmp)
            val dateDocRef = db.collection("userData")
                .document(userID!!)
                .collection("Dates")
                .document(date)
            dateDocRef.get().addOnSuccessListener {
                var recipeNotOnDay = true
                val recipeMapList = it.get("recipes")
                val recipeList: MutableList<RecipeData> = mutableListOf()
                if(recipeMapList != null){
                    for(recipe in recipeMapList as List<Map<String,Any>>){
                        recipeList.add(RecipeData(recipe))
                    }
                }
                for (recipe in recipeList){
                    if (recipe.name == data.name) recipeNotOnDay = false
                }
                if(recipeNotOnDay) dateDocRef.update("recipes", FieldValue.arrayUnion(data.getDataMap()))
            }
        }
    }

    fun removeRecipeFromDate(date: String, recipeData: RecipeData, resBody: MutableLiveData<List<RecipeData>>) {
        //use helper to correctly format data
        val data = recipeDateHelper(recipeData)
        val dateDocRef = db.collection("userData")
            .document(userID!!)
            .collection("Dates")
            .document(date)

        dateDocRef.get().addOnSuccessListener {
            dateDocRef.update("recipes", FieldValue.arrayRemove(data.getDataMap())).addOnSuccessListener {
                val updatedDateDocRef = db.collection("userData")
                    .document(userID!!)
                    .collection("Dates")
                    .document(date)
                updatedDateDocRef.get().addOnSuccessListener {
                    val recipeMapList = it.get("recipes")
                    val recipeList: MutableList<RecipeData> = mutableListOf()
                    if (recipeMapList != null){
                        for (recipe in recipeMapList as List<Map<String,Any>>){
                            recipeList.add(RecipeData(recipe))
                        }
                        resBody.value = recipeList.toList()
                    }
                }
            }
        }
    }

    private fun recipeDateHelper(recipeData: RecipeData): RecipeData {
        val foodMapList = recipeData.foodList
        val foodList: MutableList<FoodData> = mutableListOf()
        if(foodMapList != null){
            for(food in foodMapList as List<Map<String,Any>>){
                foodList.add(FoodData(food))
            }
        }
        return RecipeData(
            recipeData.name,
            recipeData.imageLink,
            recipeData.recipeImageLink,
            recipeData.rating,
            foodList
        )
    }

    fun getRecipesForDate(date: String, resBody: MutableLiveData<List<RecipeData>>) {
        val dateDocRef = db.collection("userData")
            .document(userID!!)
            .collection("Dates")
            .document(date)
        dateDocRef.get().addOnSuccessListener {
            if (it.get("recipes") != null) {
                val list: MutableList<RecipeData> = mutableListOf()
                for (recipe in it.get("recipes") as List<Map<String, Any>>) {
                    list.add(RecipeData(recipe))
                }
                resBody.value = list
            }
            else resBody.value = emptyList()
        }
    }

    //SHOPPING LIST//
    //get shopping list from firebase
    fun getShoppingList(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("shoppingList")
    }

    // Push food to shopping list in firebase.
    @RequiresApi(Build.VERSION_CODES.N)
    fun addShoppingListItem(shoppingData: ShoppingData) {
        // Create a reference to the shopping firebase document.
        val shoppingDocRef = db.collection("userData")
            .document(userID!!)
            .collection("shoppingList")
            .document(shoppingData.name)
        // Inspect the pantry data.
        shoppingDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the shopping list.
            if (it.exists()) {
                //prevents user from crashing program by exceeding the maximum size of a long
                val newQuantity = min(shoppingData.quantity + it.data!!.get("quantity") as Long, 99999)
                shoppingDocRef.update("quantity", newQuantity)
            }else{
                 shoppingDocRef.set(shoppingData)
            }
        }
    }

    // Remove a quantity of an existing food from the shopping list in firebase.
    fun removeItemQtyFromShoppingList(shoppingData: ShoppingData, quantity: Int) {
        // Create a reference to the recipe firebase document.
        val shoppingDocRef = db.collection("userData")
            .document(userID!!)
            .collection("shoppingList")
            .document(shoppingData.name)
        shoppingDocRef.get().addOnSuccessListener {
            // Check if the food already exists in the shopping list.
            val newQuantity = shoppingData.quantity - quantity
            if(newQuantity > 0){
                shoppingDocRef.update("quantity", newQuantity)
            }else{
                shoppingDocRef.delete()
            }
        }
    }
}

