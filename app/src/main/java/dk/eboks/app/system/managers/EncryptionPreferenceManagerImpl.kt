package dk.eboks.app.system.managers

import android.content.Context
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.nodes.locksmith.core.preferences.EncryptedPreferences

class EncryptionPreferenceManagerImpl(context: Context) : EncryptionPreferenceManager {
    private val encryptedPreference = EncryptedPreferences(
        context,
        "EboksEncryptedPreferences",
        Context.MODE_PRIVATE
    )

    override fun getInt(key: String, defaultValue: Int): Int {
        return encryptedPreference.getInt(key, defaultValue)
    }

    override fun setInt(key: String, value: Int) {
        encryptedPreference.putInt(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return encryptedPreference.getLong(key, defaultValue)
    }

    override fun setLong(key: String, value: Long) {
        encryptedPreference.putLong(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return encryptedPreference.getBoolean(key, defaultValue)
    }

    override fun setBoolean(key: String, value: Boolean) {
        encryptedPreference.putBoolean(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return encryptedPreference.getFloat(key, defaultValue)
    }

    override fun setFloat(key: String, value: Float) {
        encryptedPreference.putFloat(key, value)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return encryptedPreference.getString(key, defaultValue)
    }

    override fun setString(key: String, value: String) {
        encryptedPreference.putString(key, value)
    }

    override fun remove(key: String) {
        // Todo add remove method
    }

    override fun clear() {
        encryptedPreference.clear()
    }
}