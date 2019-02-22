package dk.eboks.app.system.managers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dk.eboks.app.domain.managers.PrefManager

/**
 * Created by bison on 11/10/17.
 */
class PrefManagerImpl(context: Context) : PrefManager {
    private var sharedPrefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    override fun setInt(key: String, value: Int) {
        sharedPrefs.edit().putInt(key, value).commit()
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        sharedPrefs.getLong(key, defaultValue)

    override fun setLong(key: String, value: Long) {
        sharedPrefs.edit().putLong(key, value).commit()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPrefs.getBoolean(key, defaultValue)

    override fun setBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).commit()
    }

    override fun getFloat(key: String, defaultValue: Float): Float =
        sharedPrefs.getFloat(key, defaultValue)

    override fun setFloat(key: String, value: Float) {
        sharedPrefs.edit().putFloat(key, value).commit()
    }

    override fun getString(key: String, defaultValue: String?): String? =
        sharedPrefs.getString(key, defaultValue)

    override fun setString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).commit()
    }

    override fun remove(key: String) {
        sharedPrefs.edit().remove(key).commit()
    }
}
