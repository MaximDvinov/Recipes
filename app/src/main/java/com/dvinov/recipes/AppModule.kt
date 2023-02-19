package com.dvinov.recipes

import android.content.Context
import androidx.room.Room
import com.dvinov.recipes.screens.recipes.RecipesRepository
import com.dvinov.recipes.screens.recipes.RecipesRepositoryImpl
import com.dvinov.recipes.screens.recipes.models.RecipeDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context,
    ): RecipeDatabase = Room.databaseBuilder(
        context,
        RecipeDatabase::class.java,
        "app-database"
    ).build()
}

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun providesRecipeDao(
        database: RecipeDatabase,
    ): RecipeDao = database.recipeDao()
}

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {
    @Binds
    fun bindsRecipesRepository(
        apodRepository: RecipesRepositoryImpl
    ): RecipesRepository
}