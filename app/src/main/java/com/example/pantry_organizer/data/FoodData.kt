package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class FoodData(
    val name: String,
    val apiID: String?,
    val imageLink: String?,
    val calories: Double,
    val servingQty: String,
    val servingUnit: String,
    val fat: Double,
    val sugar: Double,
    val carbs: Double,
    val protein: Double
)
{
    // Convenience constructor using a food map.
    constructor(map: Map<String, Any?>): this(
        map["name"] as String,
        map["apiID"] as String?,
        map["imageLink"] as String?,
        map["calories"] as Double,
        map["servingQty"] as String,
        map["servingUnit"] as String,
        map["fat"] as Double,
        map["sugar"] as Double,
        map["carbs"] as Double,
        map["protein"] as Double
    )

    // Convenience constructor using api food data.
    constructor(apiData: ApiFoodNutritionData): this(
        apiData.food_name!!,
        apiData.food_name,
        apiData.photo?.highres,
        apiData.nf_calories!!,
        apiData.serving_qty ?: "1",
        apiData.serving_unit ?: "count",
        apiData.nf_total_fat!!,
        apiData.nf_sugars!!,
        apiData.nf_total_carbohydrate!!,
        apiData.nf_protein!!
    )

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "apiID" to apiID,
            "imageLink" to imageLink,
            "calories" to calories,
            "servingQty" to servingQty,
            "servingUnit" to servingUnit,
            "fat" to fat,
            "sugar" to sugar,
            "carbs" to carbs,
            "protein" to protein
        )
    }
}