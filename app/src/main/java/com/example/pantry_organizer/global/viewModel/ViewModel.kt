package com.example.pantry_organizer.global.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.data.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application): AndroidViewModel(application) {
    // Firebase repository.
    private val repository = Repository()

    // Live data objects.
    val pantryList: MutableLiveData<List<PantryData>> = MutableLiveData()
    var foodPreviewList: MutableLiveData<List<FoodData>> = MutableLiveData()
    val recipeList: MutableLiveData<List<RecipeData>> = MutableLiveData()
    var singleRecipe: MutableLiveData<RecipeData> = MutableLiveData()

    init {
        getPantries()
        getRecipes()
    }

    // Populate pantry list live data from firebase data.
    fun getPantries() {
        repository.getPantries()
            .addSnapshotListener { querySnapshot, _ ->
                val list: MutableList<PantryData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    list.add(PantryData(doc))
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


//    fun getFoodPreviews(name: String){
//        repository.getFoodPreviews( foodPreviewList, name)
//    }



}