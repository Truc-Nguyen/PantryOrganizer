package com.example.pantry_organizer.data

class UserData() {
    val pantryList = ArrayList<PantryData>()

    fun addPantry(pantryData: PantryData) {
        pantryList.add(pantryData)
    }
}