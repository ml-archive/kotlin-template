package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.ViewModel
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap

open class BaseViewModel : ViewModel() {

    private val tagMap = ConcurrentHashMap<String, Any>()

    internal fun <T : Any> setTag(key: String, t: T): T {
        return getTag<T>(key)
            ?: {
                tagMap[key] = t
                t
            }.invoke()
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T> getTag(key: String) = tagMap[key] as? T

    override fun onCleared() {
        super.onCleared()
        tagMap.forEach { entry ->
            (entry.value as? Closeable)?.close()
        }
    }
}
