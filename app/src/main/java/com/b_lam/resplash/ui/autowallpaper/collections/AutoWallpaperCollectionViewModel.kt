package com.b_lam.resplash.ui.autowallpaper.collections

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b_lam.resplash.data.autowallpaper.model.AutoWallpaperCollection
import com.b_lam.resplash.data.collection.model.Collection
import com.b_lam.resplash.domain.autowallpaper.AutoWallpaperRepository
import com.b_lam.resplash.domain.collection.CollectionRepository
import com.b_lam.resplash.util.Result
import com.b_lam.resplash.util.livedata.Event
import kotlinx.coroutines.launch

// TODO: Fix this whole activity
class AutoWallpaperCollectionViewModel(
    private val autoWallpaperRepository: AutoWallpaperRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    val selectedAutoWallpaperCollections = autoWallpaperRepository.getSelectedAutoWallpaperCollections()

    val selectedAutoWallpaperCollectionIds = autoWallpaperRepository.getSelectedAutoWallpaperCollectionIds()

    val numCollectionsLiveData = autoWallpaperRepository.getNumberOfAutoWallpaperCollectionsLiveData()

    private val _addCollectionResultLiveData = MutableLiveData<Event<Result<Collection>>>()
    val addCollectionResultLiveData: LiveData<Event<Result<Collection>>> = _addCollectionResultLiveData

    private val _featuredCollectionLiveData by lazy {
        val liveData = MutableLiveData<List<AutoWallpaperCollection>>()
        getFeaturedCollections(liveData)
        return@lazy liveData
    }
    val featuredCollectionLiveData: LiveData<List<AutoWallpaperCollection>> = _featuredCollectionLiveData

    private val _popularCollectionLiveData by lazy {
        val liveData = MutableLiveData<List<AutoWallpaperCollection>>()
        getPopularCollections(liveData)
        return@lazy liveData
    }
    val popularCollectionLiveData: LiveData<List<AutoWallpaperCollection>> = _popularCollectionLiveData

    fun addAutoWallpaperCollection(collection: AutoWallpaperCollection) {
        viewModelScope.launch {
            autoWallpaperRepository.addCollectionToAutoWallpaper(collection)
        }
    }

    fun removeAutoWallpaperCollection(id: String) {
        viewModelScope.launch {
            autoWallpaperRepository.removeCollectionFromAutoWallpaper(id)
        }
    }

    fun getCollectionDetailsAndAdd(id: String) {
        viewModelScope.launch {
            val result = collectionRepository.getCollection(id)
            if (result is Result.Success) {
                autoWallpaperRepository.addCollectionToAutoWallpaper(result.value)
            }
            _addCollectionResultLiveData.postValue(Event(result))
        }
    }

    private fun getFeaturedCollections(
        liveData: MutableLiveData<List<AutoWallpaperCollection>>,
    ) {
        viewModelScope.launch {
            val result = collectionRepository.getCollections(1)
            if (result is Result.Success) {
                val autoWallpaperCollections = mapToAutoWallpaperCollectionList(result.value)
                liveData.postValue(autoWallpaperCollections)
            }
        }
    }

    private fun getPopularCollections(
        liveData: MutableLiveData<List<AutoWallpaperCollection>>,
    ) {
        viewModelScope.launch {
            val result = collectionRepository.searchCollections("wallpapers", 1)
            if (result is Result.Success) {
                val autoWallpaperCollections = mapToAutoWallpaperCollectionList(result.value.results)
                liveData.postValue(autoWallpaperCollections)
            }
        }
    }

    private fun mapToAutoWallpaperCollectionList(collections: List<Collection>): List<AutoWallpaperCollection> {
        return collections.map { collection ->
            AutoWallpaperCollection(
                id = collection.id,
                title = collection.title,
                user_name = collection.user?.name,
                cover_photo = collection.cover_photo?.urls?.regular,
                date_added = collection.published_at?.let { parseDateToMillis(it) }
            )
        }
    }

    // TODO: Fix
    @SuppressLint("NewApi")
    private fun parseDateToMillis(dateString: String): Long? {
        return try {
            val formatter = java.time.format.DateTimeFormatter.ISO_DATE_TIME
            val parsedDate = java.time.LocalDateTime.parse(dateString, formatter)
            parsedDate.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}