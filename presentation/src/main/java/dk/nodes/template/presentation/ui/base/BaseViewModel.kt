package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<A : BaseAction, C : BaseChange, S : BaseState> : ViewModel() {
    private val tag by lazy { javaClass.simpleName }

    protected abstract val initState: S
    protected abstract val reducer: Reducer<S, C>
    protected val actions: Channel<A> = Channel()

    protected var _viewState = MediatorLiveData<S>()

    val viewState: LiveData<S> = MediatorLiveData<S>().apply {
        addSource(_viewState) { data ->
            Timber.i("$tag: Received viewState: $data")
            setValue(data)
        }
    }

    protected val currentState
        get() = _viewState.value
                ?: initState // We want the state to always be non null. Initialize the state in initState our ViewModel

    protected var stateAsync
        get() = _viewState.value ?: initState
        set(value) = _viewState.postValue(value) // Sets the value asynchronously

    protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        _viewState.addSource(source, onChanged)
    }

    init {
        viewModelScope.launch {
            observeActions()
                    .scan(initState, reducer)
                    .distinctUntilChanged()
                    .collect { _viewState.postValue(it) }
        }
    }

    protected abstract fun emitAction(action: A): Flow<C>

    /**
     * Dispatches an action. This is the only way to trigger a viewState change.
     */
    fun dispatch(action: A) {
        viewModelScope.launch {
            actions.send(action)
        }
    }


    private suspend fun observeActions(): Flow<C> = flow {
        actions.consumeEach {
            emit(emitAction(it))
        }
    }.flattenConcat()


}
