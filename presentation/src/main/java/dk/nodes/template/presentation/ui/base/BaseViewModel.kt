package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.*

abstract class BaseViewModel<T> : ViewModel() {

    protected abstract val initState: T
    protected var _viewState = MediatorLiveData<T>()
    val viewState: LiveData<T> = _viewState

    protected var state
        get() = _viewState.value
                ?: initState // We want the state to always be non null. Initialize the state in initState our ViewModel
        set(value) = _viewState.setValue(value)

    protected var stateAsync
        get() = _viewState.value ?: initState
        set(value) = _viewState.postValue(value) // Sets the value asynchronously

    protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        _viewState.addSource(source, onChanged)
    }
}
