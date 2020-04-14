package com.example.pantry_organizer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.network.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.util.*

class Repository {
    // Set firebase instances.
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val fbs = Firebase.storage.reference
    val userID = auth.currentUser?.uid

    // Get pantry list from firebase.
    fun getPantries(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
    }

    // Push a new pantry to firebase.
    fun addPantry(pantryData: Map<String, Any?>){
        db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryData["name"] as String)
            .set(pantryData)
    }

    // Upload a file into firebase storage.
    fun uploadFileToStorage(filePath: String): String {
        // Generate a unique file name for this file.
        val fbsFilename = "${UUID.randomUUID()}.jpg"

        // Upload the file into firebase storage with the unique name.
        fbs.child(fbsFilename).putStream(FileInputStream(File(filePath)))

        return fbsFilename
    }

    fun addFood(pantryName: String, foodAndAmount: Pair<FoodData, Int>){
        //get existing foodList as an arraylist (if null, create arraylist)
        //append new foodData, quantity pair
        //reupload foodlist to database
        //currently non-functional
//        var listTask  =
//            db.collection("userData")
//            .document(userID!!)
//                .collection("pantryList")
//                .document(pantryName)
//                .get()
//
//        listTask.addOnCompleteListener(){
//            val listDoc = listTask.result
//            var list : MutableList<Pair<FoodData, Int>>
//            list = listDoc!!.get("foodList") as MutableList<Pair<FoodData, Int>>
//            list.add(foodAndAmount)
//
//            val newList = list.toList()
//            fbs.child("userData").child(userID!!).child("pantryList").child(pantryName).child("foodList")
//
//        }
//
//
//        Log.d("test", listTask.toString())

    }

    // Delete a pantry from firebase.
    fun deletePantry(pantryName: String) {
        db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryName)
            .delete()
    }

    //Recipe fragment code

    //get all recipes from firebase
    fun getRecipes(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
    }

    // Push a new recipe to firebase.
    fun addRecipe(recipeData: Map<String, Any?>){
        db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeData["name"] as String)
            .set(recipeData)
    }

    // Delete a recipe from firebase.
    fun deleteRecipe(recipeName: String) {
        val recipe = db.collection("userData")
            .document(userID!!)
            .collection("recipeList")
            .document(recipeName)
            .delete()

    }

    // Get single recipe as a document reference
   fun getSingleRecipe(recipeName: String,resBody:MutableLiveData<RecipeData>){
       CoroutineScope(Dispatchers.IO).launch{
           val snapshot: DocumentSnapshot = db.collection("userData").document(userID!!).collection("recipeList").document(recipeName).get().await()
           withContext(Dispatchers.Main){
               val recipe = RecipeData(
                   snapshot.get("name").toString(),
                   snapshot.get("ingredientsList").toString(),
                   snapshot.get("imageLink").toString()
               )
               resBody.value = recipe
           }
        }
    }

    //create api client
    val service = ApiClient.makeRetrofitService()

    fun getFoodPreviews(resBody: MutableLiveData<ApiFoodPreviewPackage>, food: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFoodBySearch(food)

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

    fun getFoodNutrients(resBody: MutableLiveData<ApiFoodNutritionPackage>, food: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFoodNutrients(food)

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

}

