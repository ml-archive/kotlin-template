package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel<VS : Any> : ViewModel() {
    private val job = Job()
    private val _viewState = MutableLiveData<VS>()
    val viewState: LiveData<VS> = _viewState

    protected var state: VS
        get() {
            return _viewState.value ?: {
                val initViewState = initViewState()
                _viewState.value = initViewState
                initViewState
            }.invoke()
        }
        set(value) = _viewState.postValue(value)

    protected abstract fun initViewState(): VS

    protected val disposables = CompositeDisposable()
    protected open val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        job.cancel()
    }
}