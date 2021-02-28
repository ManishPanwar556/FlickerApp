package com.example.flickerapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickerapp.Utils
import com.example.flickerapp.repository.FlickrRepository
import com.example.flickerapp.retrofit.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FlickerViewModel
: ViewModel() {
    private lateinit var repository: FlickrRepository
    private val _response = MutableLiveData<List<Photo>>()

    val response: LiveData<List<Photo>>
        get() = _response
    init {
        repository= FlickrRepository()
        updateLiveData()
    }
    private fun updateLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.getResponse(Utils.EXTRAS,Utils.API_KEY,Utils.METHOD)
            if (res.isSuccessful) {
                res.body()?.let {
                    _response.postValue(it.photos.photo)
                }
            }
        }

    }
}