package com.dvinov.recipes.screens.recipes.network

import com.dvinov.recipes.screens.recipes.models.Recipe
import retrofit2.http.GET

interface RecipeApi {
    @GET("/android-test/recipes.json")
    suspend fun getRecipes(): List<Recipe>
}
