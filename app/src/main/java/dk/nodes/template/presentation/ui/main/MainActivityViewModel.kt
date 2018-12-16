package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.presentation.base.BaseViewModel
import dk.nodes.template.util.Event
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val getPostsInteractor: GetPostsInteractor
) : BaseViewModel() {

    private val output = object : GetPostsInteractor.Output {
        override fun onPostsLoaded(posts: List<Post>) {
            _postsLiveData.postValue(posts)
        }

        override fun onError(msg: String) {
            _errorLiveData.postValue(Event(msg))
        }
    }

    private val _postsLiveData = MutableLiveData<List<Post>>()
    private val _errorLiveData = MutableLiveData<Event<String>>()
    // Facade so the view doesn't know its mutable
    val postsLiveData: LiveData<List<Post>> = _postsLiveData
    val errorLiveData: LiveData<Event<String>> = _errorLiveData

    init {
        getPostsInteractor.output = output
        getPostsInteractor.run()
    }

    override fun onCleared() {
        super.onCleared()
        getPostsInteractor.output = null
    }
}