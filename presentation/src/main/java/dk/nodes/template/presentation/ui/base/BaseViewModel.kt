package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.nodes.template.presentation.extensions.liveDataDelegate

abstract class BaseViewModel<T : Any>(initState: T) : ViewModel() {

    private val viewStateDelegate = liveDataDelegate(initState)
    protected var state by viewStateDelegate
    val viewState = viewStateDelegate.liveData

    protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        viewStateDelegate.addSource(source, onChanged)
    }
}