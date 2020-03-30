package com.example.pantry_organizer.data

data class PantryData(
    var name: String? = null
)
{
    val foodList = ArrayList<FoodData>()

    fun addFood(foodData: FoodData) {
        foodList.add(foodData)
    }
}