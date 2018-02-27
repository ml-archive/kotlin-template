package dk.eboks.app.network.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import timber.log.Timber
import java.lang.reflect.Type

/**
 * Created by bison on 26/07/17.
 */
class ListDeserializer : JsonDeserializer<List<Any>> {
    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, typeOF: Type,
                             context: JsonDeserializationContext): List<Any>
    {
        Timber.e("Got jsonElement = $jsonElement, typeOF = $typeOF")
        if (jsonElement.isJsonObject) {
            Timber.e("isJsonObject")
            val obj = jsonElement.asJsonObject
            obj?.let {
                if(it.has("items"))
                {
                    val items = it["items"]
                    Timber.e("Element has items deserializing as list")
                    return context.deserialize(items, typeOF)
                }
            }

        }
        if(jsonElement.isJsonArray)
        {
            Timber.e("isJsonarray")

        }

        throw JsonParseException("stuff went wrong")
    }

}