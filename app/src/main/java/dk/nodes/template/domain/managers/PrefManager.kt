package dk.nodes.template.domain.managers

/**
 * Created by bison on 11/10/17.
 *
 * The clean version of JosoPrefs :D (inject into and use from interactors)
 */
interface PrefManager {
    fun getInt(key: String, defaultValue: Int): Int
    fun setInt(key: String, value: Int)
    fun getLong(key: String, defaultValue: Long): Long
    fun setLong(key: String, value: Long)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun setBoolean(key: String, value: Boolean)
    fun getFloat(key: String, defaultValue: Float): Float
    fun setFloat(key: String, value: Float)
    fun getString(key: String, defaultValue: String?): String?
    fun setString(key: String, value: String)
    fun remove(key: String)
}