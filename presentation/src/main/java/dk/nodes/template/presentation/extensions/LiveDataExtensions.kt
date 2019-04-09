package dk.nodes.template.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dk.nodes.template.presentation.util.LiveEvent
import dk.nodes.template.presentation.util.EventObserver

inline fun <T> LiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T?) -> Unit
) {
    this.observe(lifecycleOwner, Observer {
        observer(it)
    })
}

inline fun <T> LiveData<T>.observeNonNull(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(lifecycleOwner, Observer {
        it?.let(observer)
    })
}

inline fun <E, T : LiveEvent<E>> LiveData<T>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (E) -> Unit
) {
    this.observe(lifecycleOwner, EventObserver {
        observer(it)
    })
}