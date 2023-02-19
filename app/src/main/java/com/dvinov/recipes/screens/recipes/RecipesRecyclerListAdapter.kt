package com.dvinov.recipes.screens.recipes

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dvinov.recipes.databinding.RecipeLayoutBinding
import com.dvinov.recipes.getDifficultyString
import com.dvinov.recipes.getMinute
import com.dvinov.recipes.screens.recipes.models.Recipe

/**
 * Данный класс - стандартная реализация адаптера для RecyclerView
 *
 * @property onImageClick - функция для отслеживания события клика по изображению,
 * параметром является url изображения
 *
 * @property onInfoClick - функция для отслеживания клика по кнопки info
 */

class RecipesRecyclerListAdapter(
    private val onImageClick: (url: String) -> Unit, private val onInfoClick: (item: Recipe) -> Unit
) : ListAdapter<Recipe, RecipesRecyclerListAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, onImageClick, onInfoClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            RecipeLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class RecipeViewHolder(var binding: RecipeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            item: Recipe,
            onImageClick: (url: String) -> Unit,
            onClick: (item: Recipe) -> Unit
        ) {
            val context = binding.root.context

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 3f
            circularProgressDrawable.centerRadius = 50f
            circularProgressDrawable.start()

            binding.apply {
                name.text = item.name
                headline.text = item.headline
                timeAndKcal.text = "${item.time.getMinute()} mins • ${item.calories}"
                difficulty.text = item.difficulty.getDifficultyString()

                info.setOnClickListener {
                    onClick(item)
                }

                if (item.thumb.isBlank()) {
                    imageView.visibility = View.GONE
                } else {
                    imageView.visibility = View.VISIBLE
                    Glide
                        .with(context)
                        .load(item.thumb)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .placeholder(circularProgressDrawable)
                        .into(imageView)
                }

                imageView.setOnClickListener {
                    onImageClick(item.image)
                }
            }
        }
    }
}

private class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}
