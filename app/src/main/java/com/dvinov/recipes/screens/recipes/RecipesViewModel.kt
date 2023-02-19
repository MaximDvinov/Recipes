package com.dvinov.recipes.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvinov.recipes.screens.recipes.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _syncState = MutableStateFlow(SynchronizationStatus.SUCCESS)
    val syncState: StateFlow<SynchronizationStatus> = _syncState.asStateFlow()

    init {
        syncRecipes()
    }

    val recipeList = repository.recipes

    private fun syncRecipes() {
        _syncState.value = SynchronizationStatus.SYNCING
        viewModelScope.launch {
            _syncState.value = repository.syncRecipe()
        }
    }
}