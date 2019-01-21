package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.models.Result
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.presentation.base.BaseViewModel
import dk.nodes.template.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        private val postsInteractor: PostsInteractor
) : BaseViewModel() {

    private val _postsLiveData = MutableLiveData<List<Post>>()
    private val _errorLiveData = MutableLiveData<Event<String>>()
    // Facade so the view doesn't know its mutable
    val postsLiveData: LiveData<List<Post>> = _postsLiveData
    val errorLiveData: LiveData<Event<String>> = _errorLiveData

    fun fetchPosts() = scope.launch {
        val result = withContext(Dispatchers.IO) { postsInteractor.run() }
        when (result) {
            is Result.Success -> _postsLiveData.value = result.data
            is Result.Error -> _errorLiveData.value = Event(Translation.error.unknownError)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}