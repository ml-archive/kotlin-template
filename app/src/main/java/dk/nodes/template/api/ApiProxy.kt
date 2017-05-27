package dk.nodes.template.api

import com.google.gson.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by bison on 20-05-2017.
 */

interface ApiProxy {
    @GET("posts") fun getPosts() : Call<List<Post>>
    @GET("photos") fun getPhotos() : Call<List<Photo>>
}

// place these functions inside an object declaration if you for some reason hate stuff not
// enclosed in objects
fun makeApiProxy() : ApiProxy
{
    val retrofit = Retrofit.Builder()
            .baseUrl(dk.bison.wt.BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(makeGson()))
            .client(makeOkHttpClient())
            .build()
    return retrofit.create(ApiProxy::class.java)
}

private fun makeOkHttpClient(): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

    return clientBuilder.build()
}

private fun makeGson() : Gson
{
    val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
    return gson
}



private class DateDeserializer : JsonDeserializer<Date> {
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
            }

        }
        throw JsonParseException("Unparseable date: \"" + jsonElement.asString
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS))
    }

    // replacement for a static member
    companion object {
        private val formatters = HashMap<String, SimpleDateFormat>()
        private val DATE_FORMATS = arrayOf("yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd")
    }
}