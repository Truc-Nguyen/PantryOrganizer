package com.example.pantry_organizer.data

import com.google.firebase.firestore.QueryDocumentSnapshot

data class FoodData(
    val name: String,
    val apiID: Long?,
    val imageLink: String?,
    val calories: Long,
    val servingSize: String,
    val fat: Long,
    val sugar: Long,
    val carbs: Long,
    val protein: Long
)
{
    // Convenience constructor using a food map.
    constructor(map: Map<String, Any?>): this(
        map["name"] as String,
        map["apiID"] as Long?,
        map["imageLink"] as String?,
        map["calories"] as Long,
        map["servingSize"] as String,
        map["fat"] as Long,
        map["sugar"] as Long,
        map["carbs"] as Long,
        map["protein"] as Long
    )

    // Convenience method for returning a map of this object.
    fun getDataMap(): Map<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to name,
            "apiID" to apiID,
            "imageLink" to imageLink,
            "calories" to calories,
            "servingSize" to servingSize,
            "fat" to fat,
            "sugar" to sugar,
            "carbs" to carbs,
            "protein" to protein
        )
    }
}

//{
//    // Convenience constructor using a firebase document object.
//    constructor(fbDoc: QueryDocumentSnapshot):
//            this(fbDoc.get("name") as String,
//                 fbDoc.get("apiID") as Long?,
//                 fbDoc.get("imageLink") as String?,
//                 fbDoc.get("calories") as Int,
//                 fbDoc.get("servingSize") as String,
//                 fbDoc.get("fat") as Int,
//                 fbDoc.get("sugar") as Int,
//                 fbDoc.get("carbs") as Int,
//                 fbDoc.get("protein") as Int)
//
//    // Convenience method for returning a map of this object.
//    fun getDataMap(): Map<String, Any?> {
//        return hashMapOf<String, Any?>(
//            "name" to name,
//            "apiID" to apiID,
//            "imageLink" to imageLink,
//            "calories" to calories,
//            "servingSize" to servingSize,
//            "fat" to fat,
//            "sugar" to sugar,
//            "carbs" to carbs,
//            "protein" to protein
//        )
//    }
//}
//
//data class FoodDataPayload(val response_code: Int, val data: List<FoodData>)