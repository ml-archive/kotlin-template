package dk.eboks.app.domain.managers

import dk.nodes.locksmith.core.exceptions.LocksmithException

interface EncryptionPreferenceManager {
    @Throws(LocksmithException::class)
    fun getInt(key: String, defaultValue: Int): Int

    @Throws(LocksmithException::class)
    fun setInt(key: String, value: Int)

    @Throws(LocksmithException::class)
    fun getLong(key: String, defaultValue: Long): Long

    @Throws(LocksmithException::class)
    fun setLong(key: String, value: Long)

    @Throws(LocksmithException::class)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    @Throws(LocksmithException::class)
    fun setBoolean(key: String, value: Boolean)

    @Throws(LocksmithException::class)
    fun getFloat(key: String, defaultValue: Float): Float

    @Throws(LocksmithException::class)
    fun setFloat(key: String, value: Float)

    @Throws(LocksmithException::class)
    fun getString(key: String, defaultValue: String?): String?

    @Throws(LocksmithException::class)
    fun setString(key: String, value: String)

    fun remove(key: String)
    fun clear()
}