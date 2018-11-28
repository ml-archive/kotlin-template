package dk.eboks.app.network.util

import com.google.gson.JsonArray
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializer
import dk.eboks.app.domain.models.channel.ekey.*
import java.lang.reflect.Type
import java.util.*


class EKeySerializer : JsonSerializer<ArrayList<BaseEkey>> {

    override fun serialize(src: ArrayList<BaseEkey>?, typeOfSrc: Type,
                           context: JsonSerializationContext): JsonElement? {
        return if (src == null)
            null
        else {
            val ja = JsonArray()
            for (bc in src) {
                val c = map.get(bc.eKeyType) ?: throw RuntimeException("Unknow class: " + bc.eKeyType)
                ja.add(context.serialize(bc, c))

            }
            ja
        }
    }

    companion object {

        private val map = TreeMap<String, Class<*>>()

        init {
            map.put("Login", Login::class.java)
            map.put("Note", Note::class.java)
            map.put("Pin", Pin::class.java)
            map.put("Ekey", Ekey::class.java)
        }
    }
}