package com.offerfactory.topandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActorDetailViewModel : ViewModel() {

    private val _actor = MutableLiveData<Actor>()
    val actor: LiveData<Actor> = _actor

    init {
        _actor.value = ActorRepository.getDefaultActor()
    }
}
