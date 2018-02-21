package dk.eboks.app.network.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bison on 26/07/17.
 */
class DateDeserializer : JsonDeserializer<Date> {
    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, typeOF: Type,
                             context: JsonDeserializationContext): Date {
        for (format in DATE_FORMATS) {

            if (!formatters.containsKey(format)) {
                formatters.put(format, SimpleDateFormat(format, Locale.getDefault()))
            }

            try {
                return formatters[format]?.parse(jsonElement.asString) ?: Date()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        throw JsonParseException("Unparseable date: \"" + jsonElement.asString
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS))
    }

    // replacement for a static member
    companion object {
        private val formatters = HashMap<String, SimpleDateFormat>()
        val DATE_FORMATS = arrayOf("yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd", "MMM dd, yyyy hh:mm:ss aaa")
    }
}