package dk.eboks.app.domain.managers

import dk.nodes.locksmith.core.exceptions.LocksmithEncryptionException

interface EncryptionPreferenceManager {
    @Throws(LocksmithEncryptionException::class)
    fun getInt(key: String, defaultValue: Int): Int

    @Throws(LocksmithEncryptionException::class)
    fun setInt(key: String, value: Int)

    @Throws(LocksmithEncryptionException::class)
    fun getLong(key: String, defaultValue: Long): Long

    @Throws(LocksmithEncryptionException::class)
    fun setLong(key: String, value: Long)

    @Throws(LocksmithEncryptionException::class)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    @Throws(LocksmithEncryptionException::class)
    fun setBoolean(key: String, value: Boolean)

    @Throws(LocksmithEncryptionException::class)
    fun getFloat(key: String, defaultValue: Float): Float

    @Throws(LocksmithEncryptionException::class)
    fun setFloat(key: String, value: Float)

    @Throws(LocksmithEncryptionException::class)
    fun getString(key: String, defaultValue: String?): String?

    @Throws(LocksmithEncryptionException::class)
    fun setString(key: String, value: String)

    fun remove(key: String)
    fun clear()
}