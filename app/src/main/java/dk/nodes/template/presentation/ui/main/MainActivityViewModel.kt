package dk.nodes.template.presentation.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val getPostsInteractor: GetPostsInteractor
) : ViewModel() {

    private val output = object : GetPostsInteractor.Output {
        override fun onPostsLoaded(posts: List<Post>) {
            _postsLiveData.postValue(posts)
        }

        override fun onError(msg: String) {
            _errorLiveData.postValue(msg)
        }
    }

    private val _postsLiveData = MutableLiveData<List<Post>>()
    private val _errorLiveData = MutableLiveData<String>()
    // Facade so the view doesnt know its mutable
    val postsLiveData: LiveData<List<Post>> = _postsLiveData
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        getPostsInteractor.output = output
        getPostsInteractor.run()
    }

    override fun onCleared() {
        super.onCleared()
        getPostsInteractor.output = null
    }
}