package com.example.pantry_organizer.network


import com.example.pantry_organizer.data.ApiFoodNutritionPackage
import com.example.pantry_organizer.data.ApiFoodPreviewPackage
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface FoodInterface {

    @Headers("x-app-id: 5705254a", "x-app-key: 76ea4edac97b177ffae9e7bb6c073c87")
    @GET("/v2/search/instant")
    suspend fun getFoodBySearch(@Query("query") query: String): Response<ApiFoodPreviewPackage>

    @Headers("x-app-id: 5705254a", "x-app-key: 76ea4edac97b177ffae9e7bb6c073c87")
    @POST("/v2/natural/nutrients") //need to check whether using @query still works with post requests
    @FormUrlEncoded
    suspend fun getFoodNutrients(@Field("query") query: String): Response<ApiFoodNutritionPackage>
}