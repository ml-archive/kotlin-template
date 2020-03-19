package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.*
import dk.nodes.template.presentation.navigation.Route
import dk.nodes.template.presentation.util.SingleEvent

abstract class BaseViewModel<T : Any>(initState: T) : ViewModel() {

    private val _navigationLiveData: MutableLiveData<SingleEvent<Route>> = MutableLiveData()
    val navigationLiveData: LiveData<SingleEvent<Route>> get() = _navigationLiveData

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

    protected fun navigateTo(route: Route) {
        _navigationLiveData.postValue(SingleEvent(route))
    }
}