package dk.nodes.template.network.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class ItemTypeAdapterFactory : TypeAdapterFactory {
    var rootContainerNames = listOf("data")

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {

        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)

        return object : TypeAdapter<T>() {

            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): T {

                var jsonElement = elementAdapter.read(`in`)
                if (jsonElement.isJsonObject) {
                    // Log.e("debug", "parsing element " + jsonElement.toString())
                    val jsonObject = jsonElement.asJsonObject
                    val entry_set = jsonObject.entrySet()
                    if (entry_set.size == 1) {
                        val key: String = entry_set.iterator().next().key ?: ""
                        val ele: JsonElement = entry_set.iterator().next().value
                        if (rootContainerNames.contains(key)) {
                            // Log.e("debug", "Doing deserialization workaround")
                            return delegate.fromJsonTree(ele)
                        }
                    }
                }
                return delegate.fromJsonTree(jsonElement)
            }
        }.nullSafe()
    }
}