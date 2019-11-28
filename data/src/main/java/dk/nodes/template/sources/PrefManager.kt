package dk.nodes.template.sources

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PrefManager @Inject constructor(context: Context) {

    private val sharedPrefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun remove(key: String) {
        sharedPrefs.edit { remove(key) }
    }

    fun clear() {
        sharedPrefs.edit { clear() }
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> get(key: String, default: T): T {
        return this.sharedPrefs.getItem(key, default)
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> set(key: String, value: T) {
        sharedPrefs.edit { setItem(key, value) }
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> observeKey(
        key: String,
        default: T
    ): Flow<T> {
        return callbackFlow {
            offer(get(key, default))

            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
                if (key == k) {
                    offer(get(key, default)!!)
                }
            }

            sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }
}

private inline fun <reified T> SharedPreferences.Editor.setItem(key: String, value: T) {
    @Suppress("UNCHECKED_CAST")
    when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        is Set<*> -> putStringSet(key, value as Set<String>)
        is MutableSet<*> -> putStringSet(key, value as MutableSet<String>)
        else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
    }
}

private inline fun <reified T> SharedPreferences.getItem(key: String, default: T): T {
    @Suppress("UNCHECKED_CAST")
    return when (default) {
        is String -> getString(key, default) as T
        is Int -> getInt(key, default) as T
        is Long -> getLong(key, default) as T
        is Boolean -> getBoolean(key, default) as T
        is Float -> getFloat(key, default) as T
        is Set<*> -> getStringSet(key, default as Set<String>) as T
        is MutableSet<*> -> getStringSet(key, default as MutableSet<String>) as T
        else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
    }
}