package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.arch.domain.interactor.launchInteractor
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.presentation.base.BaseViewModel
import dk.nodes.template.util.Event
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    getPostsInteractor: GetPostsInteractor
) : BaseViewModel() {

    private val _postsLiveData = MutableLiveData<List<Post>>()
    private val _errorLiveData = MutableLiveData<Event<String>>()
    // Facade so the view doesn't know its mutable
    val postsLiveData: LiveData<List<Post>> = _postsLiveData
    val errorLiveData: LiveData<Event<String>> = _errorLiveData

    init {
        scope.launchInteractor(getPostsInteractor, GetPostsInteractor.Input(0)) {
            when {
                it.isSuccess -> _postsLiveData.postValue(it.getOrNull())
                it.isFailure -> _errorLiveData.postValue(Event(it.toString()))
            }
        }
    }
}