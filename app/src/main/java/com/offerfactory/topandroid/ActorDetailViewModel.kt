package com.offerfactory.topandroid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ActorDetailViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_ACTOR_ID = "actor_id"
    }

    private val _actor = MutableLiveData<Actor?>()
    val actor: LiveData<Actor?> = _actor

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        val savedActorId = savedStateHandle.get<Int>(KEY_ACTOR_ID)

        if (savedActorId != null) {
            loadActor(savedActorId)
        } else {
            loadDefaultActor()
        }
    }

    fun loadActor(actorId: Int) {
        _isLoading.value = true
        _error.value = null

        savedStateHandle[KEY_ACTOR_ID] = actorId

        try {
            val loadedActor = getActorFromRepository(actorId)

            if (loadedActor != null) {
                _actor.value = loadedActor
                Log.d("ActorDetailViewModel", "Actor loaded: ${loadedActor.name}")
            } else {
                loadDefaultActor()
                _error.value = "Актёр с ID $actorId не найден, показан дефолтный"
            }
        } catch (e: Exception) {
            loadDefaultActor()
            _error.value = "Ошибка загрузки: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadDefaultActor() {
        _isLoading.value = true
        _error.value = null

        try {
            val defaultActor = ActorRepository.getDefaultActor()
            _actor.value = defaultActor
            savedStateHandle[KEY_ACTOR_ID] = defaultActor.id
            Log.d("ActorDetailViewModel", "Default actor loaded: ${defaultActor.name}")
        } catch (e: Exception) {
            _error.value = "Ошибка загрузки дефолтного актёра: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    private fun getActorFromRepository(actorId: Int): Actor? {
        val defaultActor = ActorRepository.getDefaultActor()

        return if (defaultActor.id == actorId) {
            defaultActor
        } else {
            null
        }
    }

    fun getKnownForItems(): List<String> {
        return _actor.value?.knownFor?.take(5) ?: emptyList()
    }

    fun getWikiUrl(): String? {
        return _actor.value?.wikiUrl
    }

    fun getCurrentActor(): Actor? {
        return _actor.value
    }

    fun refresh() {
        val currentId = savedStateHandle.get<Int>(KEY_ACTOR_ID)
        currentId?.let { loadActor(it) }
    }

    fun isActorLoaded(): Boolean {
        return _actor.value != null
    }
}