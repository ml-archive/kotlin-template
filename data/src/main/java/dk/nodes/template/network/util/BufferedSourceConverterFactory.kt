package dk.nodes.template.network.util

import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/*
    This is necessary to get general purpose disk caching from Store library to work
    with retrofit. (Store expects okio BufferedSource)
 */
class BufferedSourceConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *>? {
        return if (BufferedSource::class.java != type) {
            null
        } else Converter<ResponseBody, BufferedSource> { value -> value.source() }
    }
}