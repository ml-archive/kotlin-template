package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.*
import androidx.navigation.NavController
import dk.nodes.template.presentation.ui.splash.SplashViewState
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.*
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap

abstract class BaseViewModel<T> : ViewModel() {

    abstract val initState: T
    var viewState = MediatorLiveData<T>()
        protected set

     var state
        get() = viewState.value
                ?: initState    // We want the state to always be non null. Initialize the state in initState our ViewModel
        set(value) = viewState.setValue(value)

    var stateAsync
        get() = viewState.value ?: initState
        set(value) = viewState.postValue(value) // Sets the value asynchronously

    fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        viewState.addSource(source, onChanged)
    }

}
