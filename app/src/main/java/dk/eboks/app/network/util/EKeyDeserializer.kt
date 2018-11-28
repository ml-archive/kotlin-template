package dk.eboks.app.network.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import dk.eboks.app.domain.models.channel.ekey.*
import java.lang.reflect.Type
import java.util.*


class EKeyDeserializer : JsonDeserializer<List<BaseEkey>> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<BaseEkey> {

        val list = ArrayList<BaseEkey>()
        val ja = json.asJsonArray

        for (je in ja) {
            val type = je.asJsonObject.get("eKeyType").asString
            val c = map.get(type) ?: throw RuntimeException("Unknow class: $type")
            list.add(context.deserialize<BaseEkey>(je, c))
        }

        return list
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