package com.example.pantry_organizer.global.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.data.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application): AndroidViewModel(application) {
    // Firebase repository (and Api)
    private val repository = Repository()

    // Live data objects.
    val pantryList: MutableLiveData<List<PantryData>> = MutableLiveData()
    var foodPreviewList: MutableLiveData<ApiFoodPreviewPackage> = MutableLiveData()
    var apiFoodNutrients: MutableLiveData<ApiFoodNutritionPackage> = MutableLiveData()
    val recipeList: MutableLiveData<List<RecipeData>> = MutableLiveData()
    var singleRecipe: MutableLiveData<RecipeData> = MutableLiveData()
    var singleFood: MutableLiveData<FireBaseFood> = MutableLiveData()
    val singlePantryFoods: MutableLiveData<List<String>> = MutableLiveData()
    var foodList: MutableLiveData<List<FireBaseFood>> = MutableLiveData()

    init {
        getPantries()
        getRecipes()
        getFoods()
    }

    // Populate pantry list live data from firebase data.
    fun getPantries() {
        repository.getPantries()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<PantryData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    if (doc.get("name") != null) {
                        list.add(PantryData(doc))
                    }
                }
                pantryList.value = list
            }
    }

    //get details of a pantry (ie: foodList)
    fun getSinglePantryFoods(pantryName: String) {
        repository.getSinglePantryFoods(pantryName,singlePantryFoods)
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

    // Upload a file to firebase. Returns the unique name of the file.
    fun uploadFileToStorage(filePath: String): String {
        return repository.uploadFileToStorage(filePath)
    }

    // Delete a pantry from firebase.
    fun deletePantry(pantryName: String) {
        repository.deletePantry(pantryName)
    }

    //Recipes Fragment

    //get recipes from firebase
    fun getRecipes() {
        repository.getRecipes()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<RecipeData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    list.add(RecipeData(doc))
                }
                recipeList.value = list
            }
    }

    // Push a new recipe to firebase.
    fun addRecipe(recipeData: Map<String, Any?>): Boolean {
        // Check for existing pantry with duplicate name.
        if (recipeList.value != null) {
            for (recipe in recipeList.value!!) {
                if (recipe.name == recipeData["name"]) {
                    return false
                }
            }
        }

        // Push the new pantry data to firebase.
        repository.addRecipe(recipeData)
        return true
    }

    // Delete a pantry from firebase.
    fun deleteRecipe(recipeName: String) {
        repository.deleteRecipe(recipeName)
    }

    //get details of a recipe
    fun getSingleRecipe(recipeName: String) {
        repository.getSingleRecipe(recipeName,singleRecipe)
    }


    //get food previews from the nutritionix api
    fun getFoodPreviews(query: String){
        repository.getFoodPreviews( foodPreviewList, query)
    }

    // get food nutrient information from the nutritionix api
    fun getFoodNutrientsFromApi(query: String){
        repository.getFoodNutrientsFromApi(apiFoodNutrients, query)
    }
    //Foods fragments
    fun getFoods() {
        repository.getFoods()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<FireBaseFood> = mutableListOf()
                for (doc in querySnapshot!!) {
                    list.add(FireBaseFood(doc))
                }
                foodList.value = list
            }
    }

    //get details of a single food
    fun getSingleFood(foodName: String) {
        Log.d("vmaddfood",foodName)
        repository.getSingleFood(foodName,singleFood)
        Log.d("vmaddfood",singleFood.value.toString())
    }

    fun addFoodToPantry(pantryName: String, foodAndAmount: String): Boolean{
        repository.addFoodToPantry(pantryName,foodAndAmount,singlePantryFoods)
        return true
    }

    fun addFoodToFirebase(foodData: Map<String, Any?>): Boolean {
        // Check for existing pantry with duplicate name.
        Log.d("vmaddfoodfirebase",foodList.value.toString())
        if (foodList.value != null) {
            for (food in foodList.value!!) {
                if (food.food_name == foodData["name"]) {
                    return false
                }
            }
        }
        // Push the new pantry data to firebase.
        repository.addFoodToFirebase(foodData)
        Log.d("vmaddfoodfirebase","added to firebaes")
        return true
    }




//    fun getFoodPreviews(name: String){
//        repository.getFoodPreviews( foodPreviewList, name)
//    }



}