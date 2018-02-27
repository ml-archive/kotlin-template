package dk.eboks.app.network.util
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import timber.log.Timber
import java.io.IOException

/**
 * Created by bison on 26/07/17.
 */
class ItemTypeAdapterFactory : TypeAdapterFactory {
    var rootContainerNames = listOf("data", "items")

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {

        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)
        var listElement: JsonElement? = null
        var metadata: JsonElement? = null

        return object : TypeAdapter<T>() {

            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): T {

                var jsonElement = elementAdapter.read(`in`)
                if (jsonElement.isJsonObject) {
                    //Timber.e("parsing element " + jsonElement.toString())
                    val jsonObject = jsonElement.asJsonObject
                    val entry_set = jsonObject.entrySet()
                    listElement = null
                    metadata = null
                    if(entry_set.isNotEmpty())
                    {
                        for(entry in entry_set)
                        {
                            val key : String = entry_set.iterator().next().key ?: ""
                            val ele : JsonElement = entry_set.iterator().next().value
                            if(rootContainerNames.contains(key))
                            {
                                Timber.e("Got that special list, looking for meta")
                                listElement = ele
                                //Timber.e("Doing deserialization workaround")
                            }
                            if(key.contentEquals("metadata"))
                            {
                                if(ele.isJsonObject)
                                {
                                    metadata = ele
                                    Timber.e("Found metadata object")
                                }
                            }
                        }
                        if(listElement != null)
                        {
                            return delegate.fromJsonTree(listElement)
                        }
                    }
                }
                return delegate.fromJsonTree(jsonElement)
            }
        }.nullSafe()
    }
}