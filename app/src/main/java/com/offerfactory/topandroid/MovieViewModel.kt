package com.offerfactory.topandroid
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class MovieViewModel : ViewModel() {
    private val _currentMovie = MutableLiveData<Movie>()

}