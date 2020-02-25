package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.nodes.template.presentation.extensions.LiveDataDelegate

abstract class BaseViewModel<T : Any>(initState: T) : ViewModel() {

    val viewState = LiveDataDelegate(initState)
    protected var state by viewState

    protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        viewState.addSource(source, onChanged)
    }
}