package dk.eboks.app.storage.base

import android.content.Context
import com.google.gson.Gson
import dk.eboks.app.BuildConfig
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

/**
 * Created by bison on 01-07-2017.
 * Derive from this to save json files the easy way, see warningapp-kotlin for usage
 */
abstract class GsonFileStorageRepository<T>(val context: Context, val gson: Gson, val filename : String) {

    fun save(objects: T) {
        val outputStream: FileOutputStream
        try {
            val json = gson.toJson(objects)
            Timber.i("Saving cache file $filename")
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(json.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            throw(RuntimeException("file could not be written to internal storage"))
        }
    }

    fun load(type : Type): T {
        val inputStream: FileInputStream
        try {
            inputStream = context.openFileInput(filename)
            val bufferSize = 1024
            val buffer = CharArray(bufferSize)
            val out = StringBuilder()
            val `in` = InputStreamReader(inputStream, "UTF-8")
            while (true) {
                val rsz = `in`.read(buffer, 0, buffer.size)
                if (rsz < 0)
                    break
                out.append(buffer, 0, rsz)
            }
            val json = out.toString()
            Timber.i("Loaded cache file $filename")
            val objects : T = gson.fromJson(json, type)
            return objects
        }
        catch (e : Exception)
        {
            //if(BuildConfig.DEBUG)
            //    e.printStackTrace()
            Timber.e("Caching file not found ($filename)")
            throw(RuntimeException("File $filename could not be read from internal storage"))
        }
    }
}