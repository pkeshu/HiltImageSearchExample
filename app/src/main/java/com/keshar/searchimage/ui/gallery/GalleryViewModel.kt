package com.keshar.searchimage.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.keshar.searchimage.repo.UnsplashRepository

class GalleryViewModel @ViewModelInject constructor(
    private val repo: UnsplashRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
    val photos = currentQuery.switchMap { currentQuery ->
        repo.getSearchResult(currentQuery).cachedIn(viewModelScope)
    }

    fun sarchPhotos(query: String) {
        currentQuery.value = query
    }

}