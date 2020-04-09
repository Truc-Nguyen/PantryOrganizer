package com.example.pantry_organizer.global.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.data.Repository

class ViewModel(application: Application): AndroidViewModel(application) {
    // Firebase repository.
    private val repository = Repository()

    // Live data objects.
    var pantryList: MutableLiveData<List<PantryData>> = MutableLiveData()

    init {
        pantryList = getPantries()
    }

    private fun getPantries(): MutableLiveData<List<PantryData>> {
        repository.getUserPantries()
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                var list: MutableList<PantryData> = mutableListOf()
                for (doc in querySnapshot!!) {
                    Log.d("aoeu",doc.get("name").toString())
                    if (doc.get("name") != null) {
                        list.add(PantryData(doc))
                    }
                }
                pantryList.value = list
            }
        return pantryList
    }
}