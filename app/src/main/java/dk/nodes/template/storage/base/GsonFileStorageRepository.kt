package dk.nodes.template.storage.base

import android.content.Context
import com.google.gson.Gson
import dk.nodes.template.repositories.RepositoryException
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

abstract class GsonFileStorageRepository<T>(
    val context: Context,
    val gson: Gson,
    val filename: String
) {

    @Throws(RepositoryException::class)
    fun save(objects: T) {
        val outputStream: FileOutputStream
        try {
            val json = gson.toJson(objects)
            Timber.d("Saving json = $json")
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(json.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            throw(RepositoryException(
                -1,
                "file could not be written to internal storage"
            ))
        }
    }

    @Throws(RepositoryException::class)
    fun load(type: Type): T {
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
            Timber.d("Read json = $json")
            val objects: T = gson.fromJson(json, type)
            return objects
        } catch (e: Exception) {
            Timber.e("Catching file not found")
            throw(RepositoryException(
                -1,
                "File $filename could not be read from internal storage"
            ))
        }
    }
}