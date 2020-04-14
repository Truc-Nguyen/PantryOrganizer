package com.example.pantry_organizer.data

data class ApiFoodPreviewPackage (val common: List<ApiFoodPreview>)

data class ApiFoodPreview(
    val food_name: String,
    val serving_unit: String,
    val tag_name: String,
    val serving_quantity: String,
    val common_type: String,
    val tag_id: String,
    val photo: Photo,
    val locale: String)

data class Photo( //there are other fields here, but I have chosen to ignore them for the time being
    val thumb: String,
    val highres: String?
)

data class ApiFoodNutritionPackage(val foods: List<ApiFoodNutrition>)

data class ApiFoodNutrition(
    val food_name: String,
    val serving_qty: String,
    val serving_unit: String,
    val serving_weight_grams: String,
    val nf_calories: Double,
    val nf_total_fat: Double,
    val nf_total_carbohydrate: Double,
    val nf_sugars: Double,
    val nf_protein: Double,
    val photo: Photo
)