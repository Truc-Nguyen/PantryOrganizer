package com.example.pantry_organizer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    // Delete a pantry from firebase.
    fun deletePantry(pantryName: String) {
        val pantry = db.collection("userData")
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
   fun getSingleRecipe(recipname: String,resBody:MutableLiveData<RecipeData>){
        Log.d("repogetsingle", "entered")
        Log.d("repogetsingle", recipname)

       CoroutineScope(Dispatchers.IO).launch{
           Log.d("coroutine", "started")
           val snapshot: DocumentSnapshot = db.collection("userData").document(userID!!).collection("recipeList").document(recipname).get().await()
           Log.d("coroutine", "ended")
           withContext(Dispatchers.Main){
               Log.d("coroutine", "in with context")
               Log.d("coroutine", snapshot.get("name").toString())

               val recipe = RecipeData(
                   snapshot.get("name").toString(),
                   snapshot.get("ingredientsList").toString(),
                   snapshot.get("imageLink").toString()
               )
               Log.d("coroutine", recipe.ingredientsList)
               resBody.value = recipe

              // resBody.value = snapshot.toObject<RecipeData>()!!
               Log.d("coroutine", "toObject")
           }
        }
    }

    suspend fun getRecipeFromFireStore(recipeName : String)
            : DocumentSnapshot?{
        Log.d("suspendfxn","entered" )
        return try{
            val data = db
                .collection("userData")
                .document(userID!!)
                .collection("recipeList")
                .document(recipeName)
                .get()
                .await()
            Log.d("suspendfxn","exited" )
            data
        }catch (e : Exception){
            null
        }
    }


}