package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged

abstract class BaseViewModel<T : Any>(initState: T) : ViewModel() {

    private val _viewState = MediatorLiveData<T>().apply { value = initState }
    val viewState = _viewState.distinctUntilChanged()
    protected var state
        get() = _viewState.value!!
        set(value) {
            _viewState.value = value
        }

    protected var stateAsync: T = state
        set(value) {
            _viewState.postValue(value) // Sets the value asynchronously
        }

    protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        _viewState.addSource(source, onChanged)
    }
}