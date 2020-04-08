package com.example.pantry_organizer.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Repository {
    // Set firebase instances.
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val fbs = Firebase.storage.reference
    val userID = auth.currentUser?.uid

    fun getUserPantries(): CollectionReference {
        return db.collection("userData")
            .document(userID!!)
            .collection("pantryList")
    }
}