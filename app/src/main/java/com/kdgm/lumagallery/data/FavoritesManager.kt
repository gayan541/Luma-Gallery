package com.kdgm.lumagallery.data

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun addFavorite(imageId: Long) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(imageId)
        saveFavorites(favorites)
    }

    fun removeFavorite(imageId: Long) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(imageId)
        saveFavorites(favorites)
    }

    fun toggleFavorite(imageId: Long): Boolean {
        val isFavorite = isFavorite(imageId)
        if (isFavorite) {
            removeFavorite(imageId)
        } else {
            addFavorite(imageId)
        }
        return !isFavorite
    }

    fun isFavorite(imageId: Long): Boolean {
        return getFavorites().contains(imageId)
    }

    fun getFavorites(): Set<Long> {
        val favoritesString = prefs.getString("favorite_ids", "") ?: ""
        return if (favoritesString.isEmpty()) {
            emptySet()
        } else {
            favoritesString.split(",").mapNotNull { it.toLongOrNull() }.toSet()
        }
    }

    private fun saveFavorites(favorites: Set<Long>) {
        prefs.edit().putString("favorite_ids", favorites.joinToString(",")).apply()
    }
}
