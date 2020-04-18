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
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val fbs = Firebase.storage.reference
    val userID = auth.currentUser?.uid

    // Upload a file into firebase storage.
    fun uploadFileToStorage(filePath: String): String {
        // Generate a unique file name for this file.
        val fbsFilename = "${UUID.randomUUID()}.jpg"

        // Upload the file into firebase storage with the unique name.
        fbs.child(fbsFilename).putStream(FileInputStream(File(filePath)))

        return fbsFilename
    }

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

    // Push food to firebase.
    fun addFood(pantryName: String, foodData: Map<String, Any?> ) {
        db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
            .document(pantryName)
            .update("foodList", FieldValue.arrayUnion(foodData))
    }

//    //Get single pantry, called in view model to get food list
//    fun getSinglePantryFoods(pantryName: String,resBody:MutableLiveData<List<List<String>>>){
//        CoroutineScope(Dispatchers.IO).launch{
//            val snapshot: DocumentSnapshot = db.collection("userData").document(userID!!).collection("pantryList").document(pantryName).get().await()
//            withContext(Dispatchers.Main){
//                var pantryFoodList = snapshot.get("foodList") as List<List<String>>?
//                Log.d("repogetsinglepantry",pantryFoodList.toString())
////                if (pantryFoodList == null) {
////                    pantryFoodList = emptyList()
////                }
//                resBody.value = pantryFoodList
//            }
//        }
//    }





    fun addFoodToFirebase(foodData: Map<String, Any?>){
        Log.d("repoaddfood","entered")
        db.collection("userData")
            .document(userID!!)
            .collection("foodList")
            .document(foodData["food_name"] as String)
            .set(foodData)
        Log.d("repoaddfood","finished")
    }

//    fun addFoodToPantry(pantryName: String, foodAndAmount: List<String>, resBody: MutableLiveData<List<List<String>>>) {
//        var updatedFoodList: List<List<String>> = emptyList()
//        Log.d("repoaddfood",foodAndAmount[0])
//        Log.d("repoaddfood",foodAndAmount[1])
//
//        CoroutineScope(Dispatchers.IO).launch{
//            //get initial food list
//            val snapshot: DocumentSnapshot = db.collection("userData")
//                    .document(userID!!).collection("pantryList")
//                    .document(pantryName).get().await()
//            withContext(Dispatchers.Main){
//                var tempFoodList = snapshot.get("foodList") as MutableList<List<String>>
//                Log.d("repoaddfood","before adding: " + tempFoodList.toString())
//                tempFoodList.add(foodAndAmount)
//
//                updatedFoodList = tempFoodList.toList()
////                val updatedPantry = PantryData(
////                    snapshot.get("name").toString(),
////                    snapshot.get("location").toString(),
////                    snapshot.get("imageLink").toString(),
////                    updatedFoodList
////                )
//                Log.d("repoaddfood","after adding: " + updatedFoodList.toString())
//
//                snapshot.reference.update("foodList",updatedFoodList)
//
//                resBody.value = updatedFoodList
//            }
//        }

//            val listDoc = listTask.result
//            var list : MutableList<Pair<_root_ide_package_.com.example.pantry_organizer.data.ApiFoodNutrition, Int>>
//            list = listDoc!!.get("foodList") as MutableList<Pair<_root_ide_package_.com.example.pantry_organizer.data.ApiFoodNutrition, Int>>
//            list.add(foodAndAmount)
//
//            val newList = list.toList()
//            fbs.child("userData").child(userID!!).child("pantryList").child(pantryName).child("foodList")
//
//    }



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

//    // Get single recipe as a document reference
//   fun getSingleRecipe(recipeName: String,resBody:MutableLiveData<RecipeData>){
//       CoroutineScope(Dispatchers.IO).launch{
//           val snapshot: DocumentSnapshot = db.collection("userData").document(userID!!).collection("recipeList").document(recipeName).get().await()
//           withContext(Dispatchers.Main){
//               val recipe = RecipeData(
//                   snapshot.get("name").toString(),
//                   snapshot.get("ingredientsList").toString(),
//                   snapshot.get("imageLink").toString()
//               )
//               resBody.value = recipe
//           }
//        }
//    }
//
//    // Get single food as a document reference
//    fun getSingleFood(foodName: String,resBody:MutableLiveData<ApiFoodNutrition>){
//        CoroutineScope(Dispatchers.IO).launch{
//            val snapshot: DocumentSnapshot = db.collection("userData").document(userID!!).collection("foodList").document(foodName).get().await()
//            withContext(Dispatchers.Main){
//                val food = ApiFoodNutrition(
//                    snapshot.get("serving_qty").toString(),
//                    snapshot.get("serving_unit").toString(),
//                    snapshot.get("nf_calories").toString().toDouble(),
//                    snapshot.get("nf_total_fat").toString().toDouble(),
//                    snapshot.get("nf_total_carbohydrate").toString().toDouble(),
//                    snapshot.get("nf_sugars").toString().toDouble(),
//                    snapshot.get("nf_protein").toString().toDouble(),
//                    snapshot.get("photo") as Photo,
//                    snapshot.get("food_name").toString()
//                )
//                resBody.value = food
//            }
//        }
//    }

    // Get all food data objects from firebase.
    fun getFoods(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("foodList")
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
            Log.d("aoeu", food)
            Log.d("aoeu", response.toString())
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        resBody.value = response.body()
                        Log.d("aoeu", resBody.value.toString())
                    }
                } catch (e: HttpException) {
                    Log.d("aoeu", "failed")
                    println("Http error")
                }
            }
        }
    }

}

