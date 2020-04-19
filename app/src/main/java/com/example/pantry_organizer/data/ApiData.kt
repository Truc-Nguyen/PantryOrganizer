package com.example.pantry_organizer.data

data class ApiFoodDataPayload(val common: List<ApiFoodData>)
data class ApiFoodData(
    val food_name: String,
    val serving_unit: String,
    val serving_quantity: String,
    val photo: Photo)

data class Photo(
    val thumb: String?,
    val highres: String?
)

data class ApiFoodNutritionPayload(val foods: List<ApiFoodNutritionData>)
data class ApiFoodNutritionData(
    val serving_qty: String?,
    val serving_unit: String?,
    val nf_calories: Double?,
    val nf_total_fat: Double?,
    val nf_total_carbohydrate: Double?,
    val nf_sugars: Double?,
    val nf_protein: Double?,
    val photo: Photo?,
    val food_name: String?
)