package com.dvinov.recipes.screens.recipes.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity("recipes")
data class Recipe(
    @PrimaryKey @SerializedName("id") val id: String = "",
    @SerializedName("calories") val calories: String = "",
    @SerializedName("carbos") val carbos: String = "",
    @SerializedName("country") val country: String? = null,
    @SerializedName("description") val description: String = "",
    @SerializedName("difficulty") val difficulty: Int = 0,
    @SerializedName("fats") val fats: String = "",
    @SerializedName("headline") val headline: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("proteins") val proteins: String = "",
    @SerializedName("thumb") val thumb: String = "",
    @SerializedName("time") val time: String = ""
) : Serializable