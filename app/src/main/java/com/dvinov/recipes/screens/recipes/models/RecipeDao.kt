package com.dvinov.recipes.screens.recipes.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow



@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getRecipes(): Flow<List<Recipe>>

    @Upsert
    suspend fun upsertRecipe(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecipe(item: Recipe)
}