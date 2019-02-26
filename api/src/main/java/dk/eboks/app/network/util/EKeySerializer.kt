package dk.eboks.app.network.util

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.TreeMap

class EKeySerializer : JsonSerializer<ArrayList<BaseEkey>> {

    override fun serialize(
        src: ArrayList<BaseEkey>?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement? {
        return if (src == null)
            null
        else {
            val ja = JsonArray()
            for (bc in src) {
                val c =
                    map[bc.eKeyType] ?: throw RuntimeException("Unknow class: " + bc.eKeyType)
                ja.add(context.serialize(bc, c))
            }
            ja
        }
    }

    companion object {

        private val map = TreeMap<String, Class<*>>()

        init {
            map["Login"] = Login::class.java
            map["Note"] = Note::class.java
            map["Pin"] = Pin::class.java
            map["Ekey"] = Ekey::class.java
        }
    }
}