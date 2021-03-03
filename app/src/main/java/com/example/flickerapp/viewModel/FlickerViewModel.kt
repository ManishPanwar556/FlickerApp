package com.example.flickerapp.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickerapp.utils.Utils
import com.example.flickerapp.repository.FlickrRepository
import com.example.flickerapp.retrofit.FlickerResponse
import com.example.flickerapp.retrofit.Photo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FlickerViewModel
    : ViewModel() {
    private lateinit var repository: FlickrRepository
    private val _response = MutableLiveData<List<Photo>>()
    var page: Int = 1
    private var _networkStatus=MutableLiveData<Boolean>()
    val networkStatus:LiveData<Boolean>
    get() = _networkStatus
    val response: LiveData<List<Photo>>
        get() = _response
    private var _searchResponse = MutableLiveData<List<Photo>>()
    val searchResponse: LiveData<List<Photo>>
        get() = _searchResponse

    init {
        repository = FlickrRepository()
    }
    fun updateNetworkStatus(status: Boolean){
        _networkStatus.postValue(status)
    }
    fun updateLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.getResponse("$page", Utils.EXTRAS, Utils.API_KEY, Utils.METHOD)
            if (res.isSuccessful) {
                res.body()?.let {
                    _response.postValue(it.photos.photo)
                }
                page++
            }
        }

    }
    fun updateSearchResponse(list:List<Photo>){
        _searchResponse.postValue(list)
    }
    fun getSearchFromNetwork(text: String): Observable<List<Photo>> {
        val observer = Observable.create(ObservableOnSubscribe<List<Photo>> {
            viewModelScope.launch(Dispatchers.IO) {
                val res = repository.getSearch("1", Utils.EXTRAS, Utils.API_KEY, Utils.SEARCH_METHOD, text)
                if (res.isSuccessful&&res.body()?.photos?.photo!=null) {
                    it.onNext(res.body()?.photos?.photo)
                    it.onComplete()
                }

            }


        })
        return observer
    }
}