package com.example.pantry_organizer.global.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.data.Repository
import com.google.android.gms.tasks.Task

class ViewModel(application: Application): AndroidViewModel(application) {
    // Firebase repository.
    private val repository = Repository()

    // Live data objects.
    val pantryList: MutableLiveData<List<PantryData>> = MutableLiveData()

    init {
        getPantries()
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
}