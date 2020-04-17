package com.example.pantry_organizer.data

import android.provider.ContactsContract
import com.google.firebase.firestore.QueryDocumentSnapshot

data class ApiFoodPreviewPackage (val common: List<ApiFoodPreview>)

data class ApiFoodPreview(
    val food_name: String,
    val serving_unit: String,
    val tag_name: String,
    val serving_quantity: String,
    val common_type: String,
    val tag_id: String,
//    val photo: Photo,
    val locale: String)

data class Photo( //there are other fields here, but I have chosen to ignore them for the time being
    val thumb: String?,
    val highres: String?
)

data class ApiFoodNutritionPackage(val foods: List<ApiFoodNutrition>)

data class ApiFoodNutrition(

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
{
    // Convenience constructor using a firebase document object.
    constructor(fbDoc: QueryDocumentSnapshot):
            this(
                fbDoc.get("serving_qty") as String?,
                fbDoc.get("serving_unit") as String?,
                fbDoc.get("nf_calories") as Double?,
                fbDoc.get("nf_total_fat") as Double?,
                fbDoc.get("nf_total_carbohydrate") as Double?,
                fbDoc.get("nf_sugars") as Double?,
                fbDoc.get("nf_protein") as Double?,
                fbDoc.get("photo") as Photo?,
                fbDoc.get("food_name") as String?
            )

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "serving_qty" to serving_qty,
            "serving_unit" to serving_unit,
            "nf_calories" to nf_calories,
            "nf_total_fat" to nf_total_fat,
            "nf_total_carbohydrate" to nf_total_carbohydrate,
            "nf_sugars" to nf_sugars,
            "nf_protein" to nf_protein,
            "photo" to photo,
            "food_name" to food_name
        )
    }

}