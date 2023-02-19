package com.dvinov.recipes.screens.recipes

import android.util.Log
import com.dvinov.recipes.screens.recipes.models.Recipe
import com.dvinov.recipes.screens.recipes.models.RecipeDao
import com.dvinov.recipes.screens.recipes.network.RecipeNetworkManager
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "RecipesRepository"

enum class SynchronizationStatus {
    SUCCESS, ERROR, SYNCING
}


interface RecipesRepository {
    val recipes: Flow<List<Recipe>>

    suspend fun syncRecipe(): SynchronizationStatus

    suspend fun addRecipe(item:Recipe)
}

class RecipesRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipesRepository {
    override val recipes: Flow<List<Recipe>> = recipeDao.getRecipes()

    // метод для синхронизации данных из сети с локальной БД
    override suspend fun syncRecipe(): SynchronizationStatus {
        return try {
            val response = RecipeNetworkManager.getRecipeApi().getRecipes()

            recipeDao.upsertRecipe(response)

            Log.i(TAG, "Recipes syncing")

            SynchronizationStatus.SUCCESS

        } catch (e: HttpException) {
            Log.e(TAG, "syncRecipe: ${e.message}")
            SynchronizationStatus.ERROR

        } catch (e: Throwable) {
            e.printStackTrace()
            SynchronizationStatus.ERROR

        }
    }

    override suspend fun addRecipe(item: Recipe) {
        recipeDao.addRecipe(item)
    }
}