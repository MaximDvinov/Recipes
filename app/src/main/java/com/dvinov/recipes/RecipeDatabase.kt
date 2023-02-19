package com.dvinov.recipes

import androidx.room.*
import com.dvinov.recipes.screens.recipes.models.Recipe
import com.dvinov.recipes.screens.recipes.models.RecipeDao

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}