package com.dvinov.recipes.screens.add_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvinov.recipes.screens.recipes.RecipesRepository
import com.dvinov.recipes.screens.recipes.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _recipe = MutableStateFlow(Recipe(id = UUID.randomUUID().toString()))
    val recipe: StateFlow<Recipe> = _recipe.asStateFlow()

    fun setCalories(calories: String) {
        _recipe.value = _recipe.value.copy(calories = calories)
    }

    fun setCarbos(carbos: String) {
        _recipe.value = _recipe.value.copy(carbos = carbos)
    }

    fun setDescription(description: String) {
        _recipe.value = _recipe.value.copy(description = description)
    }

    fun setDifficulty(difficulty: Int) {
        _recipe.value = _recipe.value.copy(difficulty = difficulty)
    }

    fun setFats(fats: String) {
        _recipe.value = _recipe.value.copy(fats = fats)
    }

    fun setHeadline(headline: String) {
        _recipe.value = _recipe.value.copy(headline = headline)
    }

    fun setImageUrl(imageUrl: String) {
        _recipe.value = _recipe.value.copy(image = imageUrl, thumb = imageUrl)
    }

    fun setName(name: String) {
        _recipe.value = _recipe.value.copy(name = name)
    }

    fun setProteins(proteins: String) {
        _recipe.value = _recipe.value.copy(proteins = proteins)
    }

    fun setTime(time: String) {
        val timeFormatted = "PT${time}M"
        _recipe.value = _recipe.value.copy(time = timeFormatted)
    }

    fun addRecipe() {
        viewModelScope.launch {
            repository.addRecipe(_recipe.value)
        }
    }

}
