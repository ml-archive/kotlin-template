package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.arch.domain.interactor.Result
import dk.nodes.arch.domain.interactor.launchInteractor
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.presentation.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    getPostsInteractor: GetPostsInteractor
) : BaseViewModel() {

    private val _postsLiveData = MutableLiveData<List<Post>>()
    private val _errorLiveData = MutableLiveData<String>()
    // Facade so the view doesn't know its mutable
    val postsLiveData: LiveData<List<Post>> = _postsLiveData
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        scope.launchInteractor(getPostsInteractor, GetPostsInteractor.Input(0)) {
            when (it) {
                is Result.Success -> _postsLiveData.postValue(it.data)
                is Result.Failure -> _errorLiveData.postValue(it.toString())
            }
        }
    }
}