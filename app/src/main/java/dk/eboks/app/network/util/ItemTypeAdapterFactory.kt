package dk.eboks.app.network.util
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dk.eboks.app.domain.models.Metadata
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
                            val key : String = entry.key
                            val ele : JsonElement = entry.value
                            //Timber.e("Examining key $key")
                            if(rootContainerNames.contains(key))
                            {
                                //Timber.e("Got that special list, looking for meta")
                                listElement = ele
                                //Timber.e("Doing deserialization workaround")
                            }
                            if(key.contentEquals("metadata"))
                            {
                                if(ele.isJsonObject)
                                {
                                    metadata = ele
                                    //Timber.e("Found metadata object")
                                }
                            }
                        }
                        if(listElement != null)
                        {
                            val listobj = delegate.fromJsonTree(listElement)
                            metadata?.let {
                                try {
                                    val meta = gson.fromJson(metadata, Metadata::class.java)
                                    _metaDataMap[listobj as Any] = meta
                                    Timber.e("Parsed metadata object = $meta")
                                }
                                catch (t : Throwable)
                                {
                                    Timber.e("Could not deserialize metadata object, ignoring...")
                                }
                            }
                            return listobj
                        }
                    }
                }
                return delegate.fromJsonTree(jsonElement)
            }
        }.nullSafe()
    }
}

private val _metaDataMap : MutableMap<Any, Metadata> = HashMap()

private fun <T> _findMetadata(list : List<T>) : Metadata?
{
    if(_metaDataMap.containsKey(list))
        return _metaDataMap[list]
    else
        return null
}

val <T> List<T>.metaData: Metadata?
    get() = _findMetadata(this)