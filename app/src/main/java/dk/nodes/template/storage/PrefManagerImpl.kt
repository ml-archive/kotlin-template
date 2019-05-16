package dk.nodes.template.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import dk.nodes.template.domain.managers.PrefManager
import javax.inject.Inject

class PrefManagerImpl @Inject constructor(context: Context) : PrefManager {
    private var sharedPrefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    override fun setInt(key: String, value: Int) {
        sharedPrefs.edit(commit = true) {
            putInt(key, value)
        }
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        sharedPrefs.getLong(key, defaultValue)

    override fun setLong(key: String, value: Long) {
        sharedPrefs.edit(commit = true) {
            putLong(key, value)
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPrefs.getBoolean(key, defaultValue)

    override fun setBoolean(key: String, value: Boolean) {
        sharedPrefs.edit(commit = true) {
            putBoolean(key, value)
        }
    }

    override fun getFloat(key: String, defaultValue: Float): Float =
        sharedPrefs.getFloat(key, defaultValue)

    override fun setFloat(key: String, value: Float) {
        sharedPrefs.edit(commit = true) {
            putFloat(key, value)
        }
    }

    override fun getString(key: String, defaultValue: String?): String? =
        sharedPrefs.getString(key, defaultValue)

    override fun setString(key: String, value: String) {
        sharedPrefs.edit(commit = true) {
            putString(key, value)
        }
    }

    override fun remove(key: String) {
        sharedPrefs.edit(true) {
            remove(key)
        }
    }
}
