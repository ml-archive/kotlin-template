package dk.nodes.template.storage

import android.content.Context
import android.content.SharedPreferences
import dk.nodes.template.BuildConfig
import dk.nodes.template.domain.managers.PrefManager

/**
 * Created by bison on 11/10/17.
 */
class PrefManagerImpl(context: Context) : PrefManager {
    var sharedPrefs : SharedPreferences = context.getSharedPreferences("${BuildConfig.APPLICATION_ID}.SHARED_PREFERENCES", Context.MODE_PRIVATE)
    var editor : SharedPreferences.Editor? = null
    override var isLocked : Boolean = false
        get() = editor != null

    override fun lock() {
        if(isLocked)
            return
        editor = sharedPrefs.edit()
    }

    override fun unlock()
    {
        if(!isLocked)
            return
        editor?.commit()
        editor = null
    }

    override fun getInt(key : String, defaultValue : Int) : Int
    {
        return sharedPrefs.getInt(key, defaultValue)
    }

    override fun setInt(key : String, value : Int)
    {
        if(!isLocked)
            return
        editor?.let {
            it.putInt(key, value)
        }
    }

    override fun getLong(key : String, defaultValue : Long) : Long
    {
        return sharedPrefs.getLong(key, defaultValue)
    }

    override fun setLong(key : String, value : Long)
    {
        if(!isLocked)
            return
        editor?.let {
            it.putLong(key, value)
        }
    }

    override fun getBoolean(key : String, defaultValue : Boolean) : Boolean
    {
        return sharedPrefs.getBoolean(key, defaultValue)
    }

    override fun setBoolean(key : String, value : Boolean)
    {
        if(!isLocked)
            return
        editor?.let {
            it.putBoolean(key, value)
        }
    }

    override fun getFloat(key : String, defaultValue : Float) : Float
    {
        return sharedPrefs.getFloat(key, defaultValue)
    }

    override fun setFloat(key : String, value : Float)
    {
        if(!isLocked)
            return
        editor?.let {
            it.putFloat(key, value)
        }
    }

    override fun getString(key : String, defaultValue : String?) : String?
    {
        return sharedPrefs.getString(key, defaultValue)
    }

    override fun setString(key : String, value : String)
    {
        if(!isLocked)
            return
        editor?.let {
            it.putString(key, value)
        }
    }

    override fun remove(key : String)
    {
        if(!isLocked)
            return
        editor?.let {
            it.remove(key)
        }
    }
}