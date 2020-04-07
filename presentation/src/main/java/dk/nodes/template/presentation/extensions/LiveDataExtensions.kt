package dk.nodes.template.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import dk.nodes.template.presentation.util.EventObserver
import dk.nodes.template.presentation.util.SingleEvent
import kotlin.reflect.KProperty

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

inline fun <E, T : SingleEvent<E>> LiveData<T>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (E) -> Unit
) {
    this.observe(lifecycleOwner, EventObserver {
        observer(it)
    })
}

class LiveDataDelegate<T : Any>(
    initialState: T,
    private val mediatorLiveData: MediatorLiveData<T> = MediatorLiveData()
) {

    init {
        mediatorLiveData.value = initialState
    }

    val liveData: LiveData<T> = mediatorLiveData

    operator fun setValue(ref: Any, p: KProperty<*>, value: T) {
        mediatorLiveData.postValue(value)
    }

    operator fun getValue(ref: Any, p: KProperty<*>): T =
        mediatorLiveData.value!!

    fun <T> addSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        mediatorLiveData.addSource(source, onChanged)
    }
}

fun <T : Any> liveDataDelegate(initialState: T) = LiveDataDelegate(initialState)